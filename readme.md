# Библиотечные значения
## Проблема
Для придания гибкости модели данных, некоторые перечисления формируются в процессе выполнения программы. Обычно для этого используется сущность содержащая строковое поле с названием, и иногда строковое поле с описанием. Реже иные поля. Далее такая сущность будет называться `библиотечной`. Хранение таких значений обычно происходит в других сущностях, далее называемых `основными`. Для сокращения количества открываемых для редактирования окон целесообразно применять выпадающие меню с возможностью добавления новых значений налету.

## Варианты решения
Наполнение библиотек и хранение библиотечных значений в полях основных сущностей может производиться по нескольким сценариям:
- Хранение объекта библиотечной сущности в виде ссылки в поле основной сущности
- Хранение строки в основной сущности, и выбор значений из названий библиотечных объектов
- Хранение произвольной строки в основной сущности, и возможность использовать "подсказки" в виде библиотечных объектов.

Первый вариант реализуется платформой CUBA "из коробки" и предполагает подключение ассоциации с библиотечной сущностью в поле основной.

Второй вариант потребует чуть больше работы с экраном редактирования, поскольку по-умолчанию компонент `LookupField` поддерживает работу с полем основной сущности на уровне объектов или перечислений. Для отображения внутри выпадающего списка простых строк необходимо инжектировать его на контроллер экрана и задать этот список при помощи вызова метода `lookupField.setOptionsList()`.

Третий сценарий будет рассмотрен ниже.

## Пример одного из вариантов
Сценарий, максимально облегчающий жизнь пользователя предполагает, что пользователь заходит на экран редактирования основной сущности, внутри основного `fieldGroup` видит поле с выпадающим списком и может выбирать из него варианты. Но это становится неудобно, когда список строк, из которых необходимо сделать выбор становится слишком большой. В этом случае пользователь может начать ввод значения с клавиатуры и произойдёт фильтрация библиотечных значений по первым введённым буквам. Для того, чтобы добавить новое значение в библиотеку, обычно, предлагается зайти на экран со списком библиотечных объектов, и создать там новый, а затем, обновив источник данных основного экрана, продолжить редактирование. Такой подход не соответствует ожиданиям пользователя и требует от него больше действий, чем он ожидает, поэтому лучше будет применить сценарий, при котором пользователь начинает ввод значения, может выбрать из отфильтрованных в выпадающем списке, а в случае отсутствия - налету добавить введённое значение к библиотеке. В примере ниже приводится код, открывающий экран редактирования бибилиотечной сущности для ввода не только имени но и описания.
В первую очередь подразумевается, что нужный компонент будет встроен в `fieldGroup`, поэтому в дескрипторе окна следует указать, что поле будет формироваться в контроллере
```xml
<fieldGroup id="fieldGroup"
            datasource="valueHolderDs">
    <!-- more options and fields here -->
        <field property="libValue"
               custom="true"
               generator="fieldGen" />
```
Таким образом получаем указание описать в контроллере метод `public Component fieldGen(Datasource, String)`, который и сгенерирует требующийся компонент для данного поля. Компонент необходимо заполнить данными, чтобы в выпадающем списке содержались все библиотечные объекты. Особенность этого процесса в том, что источники данных обновляются во время выполнения метода `public void init(Map<String, Object>)`, а генерация полей формы происходит до этого, поэтому в источнике данных в нужный момент будет лежать пустота. В связи с чем следует обновить источник данных после указания его в качестве источника для поля, выполнив `libEntitiesDs.refresh()`. Далее следует добавить возможность создавать новые библиотечные объекты `field.setNewOptionAllowed(boolean)` и описать обработчик такого добавления `field.setNewOptionHandler(String -> {})`, где `String` - это введённые пользователем символы в строку выпадающего меню. Внутри обработчика можно создавать новый библиотечный объект, открывать его для редактирования или сразу делать коммит в источник данных.
``` java
field.setNewOptionAllowed(true);
field.setNewOptionHandler(caption -> {
    LibEntity e = metadata.create(LibEntity.class);
    e.setName(caption);
    dataManager.commit(e);
    field.setValue(e.getName());
});
```

Ниже приведён полный листинг варианта, при котором открывается окно редактирования библиотечной сущности, а по закрытии и сохранении происходит установка вновь созданного значения в заголовок поля с выпадающим списком.
``` java
@Inject private ComponentsFactory componentsFactory;
@Inject private CollectionDatasource<LibEntity, UUID> libEntitiesDs;
@Inject private Datasource<ValueHolder> valueHolderDs;
@Inject private Metadata metadata;

public Component fieldGen(Datasource datasource, String fieldId) {
    LookupField field = componentsFactory.createComponent(LookupField.class);
    field.setDatasource(valueHolderDs, "libValue");
    libEntitiesDs.refresh();
    Map<String, String> map = new LinkedHashMap<>();
    libEntitiesDs.getItems().forEach(libEntity -> map.put(libEntity.getName(), libEntity.getName()));
    field.setOptionsMap(map);
    field.setNullOptionVisible(false);
    field.setNewOptionAllowed(true);
    field.setNewOptionHandler(caption -> {
        LibEntity e = metadata.create(LibEntity.class);
        e.setName(caption);
        AbstractEditor ae = openEditor(e, WindowManager.OpenType.DIALOG);
        ae.addCloseWithCommitListener(() -> {
            field.setValue(e.getName());
        });
    });
    return field;
}
```