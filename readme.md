# Библиотечные значения с дополнением
<details> <summary>Проблема</summary>
<p>
 Для придания гибкости модели данных, некоторые перечисления формируются в процессе выполнения программы. Обычно для этого используется сущность содержащая строковое поле с названием, и иногда строковое поле с описанием. Реже иные поля. Далее такая сущность будет называться `библиотечной`. Хранение таких значений обычно происходит в других сущностях, далее называемых `основными`. Для сокращения количества открываемых для редактирования окон целесообразно применять выпадающие меню с возможностью добавления новых значений налету.
</p>
</details>

<details> <summary>Варианты решения</summary>
<p>
 Наполнение библиотек и хранение библиотечных значений в полях основных сущностей может производиться по нескольким сценариям:
 - Хранение объекта библиотечной сущности в виде ссылки в поле основной сущности реализуется платформой CUBA "из коробки" и предполагает подключение ассоциации с библиотечной сущностью в поле основной.
 - Хранение строки в основной сущности, и выбор значений из названий библиотечных объектов потребует чуть больше работы с экраном редактирования, поскольку по-умолчанию компонент `LookupField` поддерживает работу с полем основной сущности на уровне объектов или перечислений. Для отображения внутри выпадающего списка простых строк необходимо инжектировать его на контроллер экрана и задать этот список при помощи вызова метода `lookupField.setOptionsList()`.
 - Хранение произвольной строки в основной сущности, и возможность использовать "подсказки" в виде библиотечных объектов будет рассмотрено ниже.
</p>
</details>

<details> <summary>Пример одного из вариантов</summary>
<p>
 Сценарий, максимально облегчающий жизнь пользователя предполагает, что пользователь заходит на экран редактирования основной сущности, внутри основного `fieldGroup` видит поле с выпадающим списком и может выбирать из него варианты. Но это становится неудобно, когда список строк, из которых необходимо сделать выбор становится слишком большой. В этом случае пользователь может начать ввод значения с клавиатуры и произойдёт фильтрация библиотечных значений по первым введённым буквам. Для того, чтобы добавить новое значение в библиотеку, обычно, предлагается зайти на экран со списком библиотечных объектов, и создать там новый, а затем, обновив источник данных основного экрана, продолжить редактирование. Такой подход не соответствует ожиданиям пользователя и требует от него больше действий, чем он ожидает, поэтому лучше будет применить сценарий, при котором пользователь начинает ввод значения, может выбрать из отфильтрованных в выпадающем списке, а в случае отсутствия - налету добавить введённое значение к библиотеке. В примере ниже приводится код, открывающий экран редактирования бибилиотечной сущности для ввода не только имени но и описания.

 <details> <summary>Дескриптор</summary>
 <p>
  В первую очередь подразумевается, что нужный компонент будет встроен в `fieldGroup`, поэтому в дескрипторе окна следует указать, что поле будет формироваться в контроллере

  ```xml
  <fieldGroup id="fieldGroup"
              datasource="valueHolderDs">
      <!-- more options and fields here -->
          <field property="libValue"
                 custom="true"
                 generator="fieldGen" />
  ```

  Таким образом получаем указание описать в контроллере метод `public Component fieldGen(Datasource, String)`, который и сгенерирует требующийся компонент для данного поля. Компонент необходимо заполнить данными, чтобы в выпадающем списке содержались все библиотечные объекты. Особенность этого процесса в том, что источники данных обновляются во время выполнения метода `public void init(Map<String, Object>)`, а генерация полей формы происходит до этого, поэтому в источнике данных в нужный момент будет лежать пустота. В связи с чем следует обновить источник данных после указания его в качестве источника для поля, выполнив `libEntitiesDs.refresh()`. Далее следует добавить возможность создавать новые библиотечные объекты `field.setNewOptionAllowed(boolean)` и описать обработчик такого добавления `field.setNewOptionHandler(String -> {})`, где `String` - это введённые пользователем символы в строку выпадающего меню.
 </p>
 </details>

 <details> <summary>Контроллер</summary>
 <p>
  Внутри обработчика можно создавать новый библиотечный объект, открывать его для редактирования или сразу делать коммит в источник данных.

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

 </p>
 </details>
</p>
</details>

# Библиотечные значения с настройкой администратором
<details> <summary>Проблема</summary>
    <p>
        Зачастую существует необходимость в регламентировании содержимого выбора библиотечных сущностей внутри основной. То есть необходимость в выборе администратором тех библиотечных объектов, которые будут подходящими для конкретных объектов основной сущности и будут даваться пользователю для выбора в основной сущности. Для этого необходимо предусмотреть специальный экран, на котором администратором будет осуществляться подобный выбор и сущность для хранения информации о соответствии.
    </p>
</details>

<details> <summary>Вариант решения со строками</summary>
    <p>
        <details> <summary>Сущность для хранения настроек</summary>
            <p>
                Для хранения необходимо предусмотреть персистентную сущность наподобие `Map<K, List<V>>`, где ключом будет название основной сущности, содержащей в себе список из возможных принимаемых значений.

                ``` java
                @NamePattern("%s|entity")
                @Table(name = "LIBFIELDSAMPLE_SETTINGS")
                @Entity(name = "libfieldsample$Settings")
                public class Settings extends StandardEntity {
                    @Column(name = "ENTITY", unique = true) protected String entity;

                    @JoinTable(name = "LIBFIELDSAMPLE_SETTINGS_LIB_ENTITY_LINK",
                        joinColumns = @JoinColumn(name = "SETTINGS_ID"),
                        inverseJoinColumns = @JoinColumn(name = "LIB_ENTITY_ID"))
                    @ManyToMany protected List<LibEntity> possible;
                ```

                Связь с набором библиотечных сущностей, естественно, должна быть многие-ко-многим. Также следует отметить, что поле ключа должно быть уникальным. Это обеспечит однозначность настроек и отсутствие дубликатов.
            </p>
        </details>

        <details> <summary>Экран настроек</summary>
            <p>
                <details> <summary>Дескриптор</summary>
                    <p>
                        На экране настроек следует учесть, что при выборе основной сущности (ключа настройки) нельзя давать пользователю возможности ошибиться, или опечататься. Такоим образом, возникает необходимость в генерировании компонента с выпадающим меню, вместо простого текстового поля со свободным вводом. Для этого необходимо предусмотреть наличие источника данных для выпадающего меню

                        ```xml
                        <dsContext>
                        <datasource id="settingsDs"
                                    class="ru.iovchinnikov.libfieldsample.entity.Settings"
                                    view="settings-view">
                            <collectionDatasource id="possibleDs"
                                                    property="possible"/>
                        </datasource>
                        <collectionDatasource id="holdersDs"
                                                class="ru.iovchinnikov.libfieldsample.entity.ValueHolder"
                                                view="valueHolder-view">
                            <query>
                                <![CDATA[select e from libfieldsample$ValueHolder e]]>
                            </query>
                        </collectionDatasource>
                        </dsContext>
                        ```

                        и, собственно, компонента с выпадающим меню

                        ``` xml
                        <fieldGroup id="fieldGroup"
                                datasource="settingsDs">
                        <column width="250px">
                            <field property="entity"
                                    custom="true"
                                    generator="entityGen"/>
                        </column>
                        </fieldGroup>
                        ```
                        
                        Который в этом случае потребует описания метода генерации компонента в контроллере.
                    </p>
                </details>

                <details> <summary>Контроллер</summary>
                    <p>
                        При создании компонента важно учесть, что на момент создания источники данных ещё не будут загружены, поэтому потребуется ручное обновление. И, поскольку поле с названием основной сущности строковое - необходимо выполнить преобразование из сущностей в источнике данных в строки `holdersDs.getItems().forEach(holder -> map.put(holder.getName(), holder.getName()));`. Полный код генератора может выглядеть как в примере:

                        ``` java
                        @Inject private ComponentsFactory componentsFactory;
                        @Inject private Datasource<Settings> settingsDs;
                        @Inject private CollectionDatasource<ValueHolder, UUID> holdersDs;

                        public Component entityGen(Datasource datasource, String fieldId) {
                            LookupField field = componentsFactory.createComponent(LookupField.class);
                            field.setDatasource(settingsDs, "entity");
                            holdersDs.refresh();
                            Map<String, String> map = new LinkedHashMap<>();
                            holdersDs.getItems().forEach(holder -> map.put(holder.getName(), holder.getName()));
                            field.setOptionsMap(map);
                            field.setNullOptionVisible(false);
                            return field;
                        }
                        ```

                    </p>
                </details>
            </p>
        </details>

        <details> <summary>Экран редактирования основной сущности</summary>
            <p>
                <details> <summary>Дескриптор</summary>
                    <p>
                        Для корректного отображения отфильтрованных библиотечных значений необходимо отфильтровать все библиотечные объекты согласно настроек администратора, для этого нужно подготовить источник данных и компонент, не предоставляющий возможности ввода случайных значений (выпадающий список). Дескриптор окна будет иметь вид (опуская стандартные сгенерированные строки)

                        ``` xml
                        <dsContext>
                        <datasource id="valueHolderDs"
                                    class="ru.iovchinnikov.libfieldsample.entity.ValueHolder"
                                    view="valueHolder-view"/>
                        <collectionDatasource id="libEntitiesDs"
                                                class="ru.iovchinnikov.libfieldsample.entity.LibEntity"
                                                view="libEntity-view">
                            <query>
                                <![CDATA[select p from libfieldsample$Settings s
                                            join s.possible p
                                            where s.entity = :custom$name]]>
                            </query>
                        </collectionDatasource>
                        </dsContext>
                        <layout expand="windowActions"
                                spacing="true">
                        <fieldGroup id="fieldGroup"
                                    datasource="valueHolderDs">
                                <field id="libValue"
                                        property="libValue"
                                        custom="true"
                                        generator="fieldGen" />
                            </column>
                        </fieldGroup>
                        ```
                        
                        Таким образом, в источнике данных будет произведена фильтрация библиотечных объектов по правилам, заданным в настройках и наименованию основного объекта, открываемого на экране, и переданного с контроллера в виде `custom` параметра (то есть данный пример также демонстрирует передачу параметров с контроллера в дескриптор).
                    </p>
                </details>

                <details> <summary>Контроллер</summary>
                    <p>
                        В связи с тем, что формирование источника данных будет происходить при инициализации экрана, а метод генерации компонента будет вызван до этого, необходимо разделить действие по генерированию компонента на два - собственно генерация компонента, и наполнение выпадающего списка данными. Для упрощения работы с генерируемым компонентом необходимо выделить его идентификатор в поле класса, таким образом он станет доступен как методу генерации, так и методу, содержащему наполняющий его код.
                        - Генерация компонента будет происходить в методе, вызываемом дескриптором

                        ``` java
                        public Component fieldGen(Datasource datasource, String fieldId) {
                            field = componentsFactory.createComponent(LookupField.class);
                            field.setDatasource(valueHolderDs, "libValue");
                            field.setNullOptionVisible(false);
                        return field;
                        ```

                        - Поскольку заполнение компонента значениями должно происходить гарантированно после формирования экрана и источников данных - можно сделать это в самом позднем из вызываемых при инициализации методов

                        ``` java
                        @Override
                        public void ready() {
                            super.ready();
                            valueHolderDs.refresh();
                            libEntitiesDs.refresh(ParamsMap.of("name", valueHolderDs.getItem().getName()));
                            Map<String, String> map = new LinkedHashMap<>();
                            libEntitiesDs.getItems().forEach(libEntity -> map.put(libEntity.getName(), libEntity.getName()));
                            field.setOptionsMap(map);
                        }
                        ```

                        Здесь следует обратить внимание на обновление источника данных и передачу ему параметра под названием `name`, а также преобразование списка сущностей в список из строк.
                    </p>
                </details>
            </p>
        </details>
    </p>
</details>

<details> <summary>Вариант решения с идентификаторами</summary>
    <p>
        <details> <summary>Сущность для хранения настроек</summary>
            <p>
                Для хранения необходимо предусмотреть персистентную сущность наподобие `Map<K, List<V>>`, где ключом будет название основной сущности, содержащей в себе список из возможных принимаемых значений.

                ``` java
                @NamePattern("%s|entity")
                @Table(name = "LIBFIELDSAMPLE_SETTINGS")
                @Entity(name = "libfieldsample$Settings")
                public class Settings extends StandardEntity {
                    @Column(name = "ENTITY", unique = true) protected String entity;

                    @JoinTable(name = "LIBFIELDSAMPLE_SETTINGS_LIB_ENTITY_LINK",
                        joinColumns = @JoinColumn(name = "SETTINGS_ID"),
                        inverseJoinColumns = @JoinColumn(name = "LIB_ENTITY_ID"))
                    @ManyToMany protected List<LibEntity> possible;
                ```

                Связь с набором библиотечных сущностей, естественно, должна быть многие-ко-многим. Также следует отметить, что поле ключа должно быть уникальным. Это обеспечит однозначность настроек и отсутствие дубликатов.
            </p>
        </details>

        <details> <summary>Экран настроек</summary>
            <p>
                <details> <summary>Дескриптор</summary>
                    <p>
                        На экране настроек следует учесть, что при выборе основной сущности (ключа настройки) нельзя давать пользователю возможности ошибиться, или опечататься. Такоим образом, возникает необходимость в генерировании компонента с выпадающим меню, вместо простого текстового поля со свободным вводом. Для этого необходимо предусмотреть наличие источника данных для выпадающего меню

                        ```xml
                        <dsContext>
                        <datasource id="settingsDs"
                                    class="ru.iovchinnikov.libfieldsample.entity.Settings"
                                    view="settings-view">
                            <collectionDatasource id="possibleDs"
                                                    property="possible"/>
                        </datasource>
                        <collectionDatasource id="holdersDs"
                                                class="ru.iovchinnikov.libfieldsample.entity.ValueHolder"
                                                view="valueHolder-view">
                            <query>
                                <![CDATA[select e from libfieldsample$ValueHolder e]]>
                            </query>
                        </collectionDatasource>
                        </dsContext>
                        ```

                        и, собственно, компонента с выпадающим меню

                        ``` xml
                        <fieldGroup id="fieldGroup"
                                datasource="settingsDs">
                        <column width="250px">
                            <field property="entity"
                                    custom="true"
                                    generator="entityGen"/>
                        </column>
                        </fieldGroup>
                        ```

                        Который в этом случае потребует описания метода генерации компонента в контроллере.
                    </p>
                </details>

                <details> <summary>Контроллер</summary>
                    <p>
                        При создании компонента важно учесть, что на момент создания источники данных ещё не будут загружены, поэтому потребуется ручное обновление. И, поскольку поле с названием основной сущности строковое - необходимо выполнить преобразование из сущностей в источнике данных в строки `holdersDs.getItems().forEach(holder -> map.put(holder.getName(), holder.getName()));`. Полный код генератора может выглядеть как в примере:

                        ``` java
                        @Inject private ComponentsFactory componentsFactory;
                        @Inject private Datasource<Settings> settingsDs;
                        @Inject private CollectionDatasource<ValueHolder, UUID> holdersDs;

                        public Component entityGen(Datasource datasource, String fieldId) {
                            LookupField field = componentsFactory.createComponent(LookupField.class);
                            field.setDatasource(settingsDs, "entity");
                            holdersDs.refresh();
                            Map<String, String> map = new LinkedHashMap<>();
                            holdersDs.getItems().forEach(holder -> map.put(holder.getName(), holder.getName()));
                            field.setOptionsMap(map);
                            field.setNullOptionVisible(false);
                            return field;
                        }
                        ```

                    </p>
                </details>
            </p>
        </details>

        <details> <summary>Экран редактирования основной сущности</summary>
            <p>
                <details> <summary>Дескриптор</summary>
                    <p>
                        Для корректного отображения отфильтрованных библиотечных значений необходимо отфильтровать все библиотечные объекты согласно настроек администратора, для этого нужно подготовить источник данных и компонент, не предоставляющий возможности ввода случайных значений (выпадающий список). Дескриптор окна будет иметь вид (опуская стандартные сгенерированные строки)

                        ``` xml
                        <dsContext>
                        <datasource id="valueHolderDs"
                                    class="ru.iovchinnikov.libfieldsample.entity.ValueHolder"
                                    view="valueHolder-view"/>
                        <collectionDatasource id="libEntitiesDs"
                                                class="ru.iovchinnikov.libfieldsample.entity.LibEntity"
                                                view="libEntity-view">
                            <query>
                                <![CDATA[select p from libfieldsample$Settings s
                                            join s.possible p
                                            where s.entity = :custom$name]]>
                            </query>
                        </collectionDatasource>
                        </dsContext>
                        <layout expand="windowActions"
                                spacing="true">
                        <fieldGroup id="fieldGroup"
                                    datasource="valueHolderDs">
                                <field id="libValue"
                                        property="libValue"
                                        custom="true"
                                        generator="fieldGen" />
                            </column>
                        </fieldGroup>
                        ```
                        
                        Таким образом, в источнике данных будет произведена фильтрация библиотечных объектов по правилам, заданным в настройках и наименованию основного объекта, открываемого на экране, и переданного с контроллера в виде `custom` параметра (то есть данный пример также демонстрирует передачу параметров с контроллера в дескриптор).
                    </p>
                </details>

                <details> <summary>Контроллер</summary>
                    <p>
                        В связи с тем, что формирование источника данных будет происходить при инициализации экрана, а метод генерации компонента будет вызван до этого, необходимо разделить действие по генерированию компонента на два - собственно генерация компонента, и наполнение выпадающего списка данными. Для упрощения работы с генерируемым компонентом необходимо выделить его идентификатор в поле класса, таким образом он станет доступен как методу генерации, так и методу, содержащему наполняющий его код.
                        - Генерация компонента будет происходить в методе, вызываемом дескриптором

                        ``` java
                        public Component fieldGen(Datasource datasource, String fieldId) {
                            field = componentsFactory.createComponent(LookupField.class);
                            field.setDatasource(valueHolderDs, "libValue");
                            field.setNullOptionVisible(false);
                        return field;
                        ```

                        - Поскольку заполнение компонента значениями должно происходить гарантированно после формирования экрана и источников данных - можно сделать это в самом позднем из вызываемых при инициализации методов
                        
                        ``` java
                        @Override
                        public void ready() {
                            super.ready();
                            valueHolderDs.refresh();
                            libEntitiesDs.refresh(ParamsMap.of("name", valueHolderDs.getItem().getName()));
                            Map<String, String> map = new LinkedHashMap<>();
                            libEntitiesDs.getItems().forEach(libEntity -> map.put(libEntity.getName(), libEntity.getName()));
                            field.setOptionsMap(map);
                        }
                        ```
                        
                        Здесь следует обратить внимание на обновление источника данных и передачу ему параметра под названием `name`, а также преобразование списка сущностей в список из строк.
                    </p>
                </details>
            </p>
        </details>
    </p>
</details>
