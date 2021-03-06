meta
====

<h2>meta is a minimal standalone Java library to be used on any target plattform like Android or Java EE. It helps to build models, views and validators</h2>

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
    <li> on the backend using Java meta model or </li>
    <li> on the frontend using JSON meta model</li>
    </ul>
  <li> Creating a view for an Pojo only based on its meta model </li>
    <ul>
    <li> for HTML </li>
    <li> for other platforms using the JSON meta model</li>
    </ul>
</ul>

Building a meta model of java model
-----------------------------------

<h3> POJO Example </h3>
Consider a simple POJO <i>SimpleObject.java</i>:

    public class SimpleObject {
      private Long id;
      private String name;
      private String private_name;
      private String description;
      private String age;
      private String unvisible_attribute;
    }
Let's create a meta model of this model:

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

Using this meta model a view can be created, because all attributes of the model are known. 

<h3> POJO Example with meta Annotations</h3>

Let's do some more complex things. Assuming that you want not only create views for your models, but also validate some of data. Moreover you don't want send some of the data to front end, because you want use it only on backend, and some of data should be visible only for owner of object. For this, simply add these annotations to the fields of <i>SimpleObject.java</i>, like:

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
    
As result, you will get this meta model:

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
This JSON object gives you more information about its model. So, you can for example validate <i>age</i> value using its regex or restrict editing of <i>private_name</i>

<h3> Complex Java Class with meta Annotations</h3>
Meta cannot only parse the standalone classes but also complex classes which uses other complex classes. Consider this example Java class with meta annotations:

    public class ComplexObject {
    	@MetaAttr(type = MetaAttr.TYPE_ID)
    	public int id;
    	@MetaAttr(type = MetaAttr.FIELDS_PRIVATE)
    	public SimpleObject simpleObject;
    	@MetaAttr(type = MetaAttr.FIELDS_NOT_NULL)
    	public LinkedList<SimpleObject> collectionOfSimpleObjects;
    	@MetaAttr(type = MetaAttr.TYPE_SKIP_META)
    	public LinkedList<SimpleObject> thisAttributeShouldNotBeConsideredByMeta;
    }

This class has a field simpleObject from Type SimpleObject. So, meta parser will go there and additionally parse the type of this field:

    {
     "FIELDS_ALL":[
        "id",
        "simpleObject",
        "collectionOfSimpleObjects"
     ],
     "FIELDS_PRIVATE":[
        "simpleObject"
     ],
     "FIELDS_COMPLEX":[
        {
          "ATTRIBUTE_TYPE":"SINGLE",
          "ATTRIBUTE_NAME":"simpleObject",
          "META_DATA":{
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
        },
        {
          "ATTRIBUTE_TYPE":"COLLECTION",
          "ATTRIBUTE_NAME":"collectionOfSimpleObjects",
          "META_DATA":{
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
        }
      ],
      "CLASS_NAME":"ComplexObject",
      "TYPE_ID":"id",
      "FIELDS_NOT_NULL":[
        "collectionOfSimpleObjects"
      ]
    }

What you can see here is that meta parsed the two complex fields (attributes) with its <i>ATTRIBUTE_NAME</i>s and its type (<i>COLLECTION</i> or <i>SINGE</i>) 

<h3>Java Sub Class</h3>
If you want to parse a class that extends another java class, then parser will parse both classes like one class. Consider this class:

    public class SubClassOfSimpleObject extends SimpleObject {
      private String id;
      @MetaAttr(regex = "true|false")
      private boolean bValue;
    }
    
There the id field of <i>SimpleObject.java</i> will be hidden by id field of <i>SubClassOfSimpleObject.java</i>, for more informations read basics of java. So, after parsing you will get this:

    {
       "FIELDS_ALL":[
          "id",
          "bValue",
          "name",
          "private_name",
          "description",
          "age"
       ],
       "FIELDS_PRIVATE":[
          "private_name"
       ],
       "CLASS_NAME":"SubClassOfSimpleObject",
       "FIELDS_REGEX":[
          {"bValue":"true|false"}
          {"age":"^-{0,1}\\d+$"}
       ],
       "FIELDS_READ_ONLY":[
          "private_name"
       ]
    }
<h3> Meta Model of meta </h3>
To understand what you can create with meta, consider this meta model:

    public class MetaModel {
    	private String CLASS_NAME;
    	private String TYPE_ID;
    	private List<String> TYPE_URL_IMAGE;
    	private List<String> TYPE_DATE_LONG;
    	private List<String> TYPE_SKIP_META;
    	private List<String> FIELDS_ALL;
    	private List<String> FIELDS_READ_ONLY;
    	private List<String> FIELDS_UNIQ_IN_SCOPE;
    	private List<String> FIELDS_NOT_NULL;
    	private List<String> FIELDS_TRANSIENT;
    	private List<String> FIELDS_PRIVATE;
    	private List<String> FIELDS_PUBLIC;
    	private List<Map<String, String>> FIELDS_REGEX;
    	private List<MetaComplexModel> FIELDS_COMPLEX;
    	class MetaComplexModel {
    		private String ATTRIBUTE_NAME;
    		private String ATTRIBUTE_TYPE;
    		private MetaModel META_DATA;
    	}
    }
    
The meta model is defined recursively. So, you can describe each model with its complex field. 

One important thing: Your model cannot have dependencies of themselves and no loops. Actually, it is clear, just thing about an object oriented data base, you cannot also store such objects in db.

Parsing a object data as JSON
-----------------------------

It's also easy to parse objects data to JSON. Consider this <i>SimpleObject</i>:

    SimpleObject so = new SimpleObject();
    so.setDescription("myDescription");
    so.setId(123L);
    so.setName("myName");
    so.setPrivateName("myPrivateName");

To parse this object just use:

    new DataParser().parseSingleObject(so);

As result you will get this JSON object:

    {
       "id":123,
       "description":"myDescription",
       "name":"myName",
       "private_name":"myPrivateName"
    }

Easy or? Assume that <i>SingleObject.java</i> has declaration like above with meta annotations. Now we can include or exclude some of the data depends on meta attributes. E.g. if we want to get only <i>MetaAttr.FIELDS_PRIVATE</i> and <i>MetaAttr.TYPE_ID</i>, we do this:

    new DataParser().parseSingleObjectIncludingMetaAttrs(so, MetaAttr.FIELDS_PRIVATE + MetaAttr.TYPE_ID);

The result is like expected:
  
    {
      "id":123,
      "private_name":"myPrivateName"
    }

Also you can parse a set of objects:

    List<SimpleObject>collectionSo = new ArrayList<SimpleObject>();
    collectionSo.add(so);
    collectionSo.add(so);
    
And parse it to JSONArray:

    new DataParser().parseCollectionObjectIncludingMetaAttrs(simpleObjects,	MetaAttr.READ_ONLY);
    
You will get the result:

    [{"private_name":"myPrivateName"},{"private_name":"myPrivateName"}]
    
This syntax of converted objects is appropriate for the REST interface conventions of most of the front-end technologies, like Backbone.js or AngularJS.
