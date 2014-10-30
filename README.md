meta
====

Portable modul (library) for any Java projects. Helps build models, views and validators
----------------------------------------------------------------------------------------

<b>meta</b> is a library, that can be used in any java project for these purposes:
<ul>
  <li> Get a meta model of any model (java class) from your project 
    <ul>
    <li> as a JSON object or</li>
    <li> as a Java object</li>
    </ul>
  </li>
  <li> Describe your models with additional meta-language given by <b>meta</b> as annotations</li>
  <li> Validate an object against its meta model </li>
    <ul>
    <li> on back end using Java meta model or </li>
    <li> on front end using JSON meta model</li>
    </ul>
  <li> Create a view for object depends on its meta model </li>
    <ul>
    <li> for HTML </li>
    <li> for other platforms using JSON meta model</li>
    </ul>
</ul>

Building a meta model of java model
-----------------------------------

It is realy easy to use <b>meta</b> for building of meta models. Consider this <i>SimpleObject.java</i> model class:

    public class SimpleObject {
      private Long id;
      private String name;
      private String private_name;
      private String description;
      private String age;
      private String unvisible_attribute;
    }
Lets create a meta model of this model:

    new MetaParser().getMetaAsJson(SimpleObject.class);
That's it. You will get as result this JSON Object:

    {
      "FIELDS_ALL":[
        "id",
        "name",
        "private_name",
        "description",
        "age",
        "unvisible_attribute"
      ],
      "CLASS_NAME":"SimpleObject"
    }

Using this meta model you can create your own view, because you know all the attributes of the model and its class name. 

Lets do some more complex things. Assuming that you want not only create views for your models, but also validate some of data. Moreover you don't want send some of the data to front end, because you want use it only on backend, and some of data should be visible only for owner of object. For this simply add these anotations to the fields of <i>SimpleObject.java</i>, like:

    public class SimpleObject {
      @MetaAttr(type = MetaAttr.TYPE_ID)
      private Long id;
      private String name;
      
      @MetaAttr(type = MetaAttr.FIELDS_PRIVATE + MetaAttr.FIELDS_READ_ONLY)
      private String private_name;
      private String description;
      
      @MetaAttr(regex = RegexUtils.NUMBER_INTEGER)
      private String age;
      
      @MetaAttr(type = MetaAttr.TYPE_SKIP_META)
      private String unvisible_attribute;
    }
    
As result you will get this meta model:

    {
      "FIELDS_ALL":[
        "id",
        "name",
        "private_name",
        "description",
        "age"
      ],
      "CLASS_NAME":"SimpleObject",
      "TYPE_ID":"id",
      "FIELDS_PRIVATE":[
        "private_name"
      ],
      "FIELDS_REGEX":[
        {"age":"^-{0,1}\\d+$"}
      ],
      "FIELDS_READ_ONLY":[
        "private_name"
      ]
    }
This JSON object gives you more information about its model. So you can for example validate <i>age</i> value using its regex or restrict editing of <i>private_name</i>
