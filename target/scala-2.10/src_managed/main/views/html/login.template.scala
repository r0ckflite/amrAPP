
package views.html

import play.templates._
import play.templates.TemplateMagic._

import play.api.templates._
import play.api.templates.PlayMagic._
import models._
import controllers._
import play.api.i18n._
import play.api.mvc._
import play.api.data._
import views.html._
/**/
object login extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template2[Form[scala.Tuple2[String, String]],Flash,play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply/*1.2*/(form: Form[(String,String)])(implicit flash: Flash):play.api.templates.HtmlFormat.Appendable = {
        _display_ {

Seq[Any](format.raw/*1.54*/("""

<html>
    <head>
    <script src=""""),_display_(Seq[Any](/*5.19*/routes/*5.25*/.Assets.at("javascripts/jquery-1.9.0.min.js"))),format.raw/*5.70*/(""""></script>

<!-- ================================== -->
<!-- twitter bootstrap -->
    <meta charset="utf-8">
    <title>Sign in &middot; AMR Configuration</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <link href=""""),_display_(Seq[Any](/*15.18*/routes/*15.24*/.Assets.at("bootstrap-3.1.1-dist/css/bootstrap.css"))),format.raw/*15.76*/("""" rel="stylesheet">
    <script src=""""),_display_(Seq[Any](/*16.19*/routes/*16.25*/.Assets.at("bootstrap-3.1.1-dist/js/bootstrap.min.js"))),format.raw/*16.79*/(""""></script>

<!-- ================================== -->
<!-- Kendo ui -->    
	<!-- Common Kendo UI Web CSS -->
 	<link href=""""),_display_(Seq[Any](/*21.16*/routes/*21.22*/.Assets.at("kendo/styles/kendo.common.min.css"))),format.raw/*21.69*/("""" rel="stylesheet" />
	<link href=""""),_display_(Seq[Any](/*22.15*/routes/*22.21*/.Assets.at("kendo/styles/kendo.rtl.min.css"))),format.raw/*22.65*/("""" rel="stylesheet" />
	<link href=""""),_display_(Seq[Any](/*23.15*/routes/*23.21*/.Assets.at("kendo/styles/kendo.silver.min.css"))),format.raw/*23.68*/("""" rel="stylesheet" />
	<link href=""""),_display_(Seq[Any](/*24.15*/routes/*24.21*/.Assets.at("kendo/styles/kendo.dataviz.min.css"))),format.raw/*24.69*/("""" rel="stylesheet" />
	<link href=""""),_display_(Seq[Any](/*25.15*/routes/*25.21*/.Assets.at("kendo/styles/kendo.dataviz.silver.min.css"))),format.raw/*25.76*/("""" rel="stylesheet" />
	

	<!-- Default Kendo UI Web theme CSS -->
	<!-- <link href=""""),_display_(Seq[Any](/*29.20*/routes/*29.26*/.Assets.at("kendo/styles/kendo.default.min.css"))),format.raw/*29.74*/("""" rel="stylesheet" /> -->
	<script src=""""),_display_(Seq[Any](/*30.16*/routes/*30.22*/.Assets.at("kendo/js/jquery.min.js"))),format.raw/*30.58*/("""")></script>
	<script src=""""),_display_(Seq[Any](/*31.16*/routes/*31.22*/.Assets.at("kendo/js/kendo.web.min.js"))),format.raw/*31.61*/(""""></script>	
<!-- ================================== -->
	
	<style type="text/css">
      body """),format.raw/*35.12*/("""{"""),format.raw/*35.13*/("""
        padding-top: 40px;
        padding-bottom: 40px;
        background-color: #f5f5f5;
      """),format.raw/*39.7*/("""}"""),format.raw/*39.8*/("""

      .form-signin """),format.raw/*41.20*/("""{"""),format.raw/*41.21*/("""
        max-width: 300px;
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      """),format.raw/*53.7*/("""}"""),format.raw/*53.8*/("""
      .form-signin .form-signin-heading,
      .form-signin .checkbox """),format.raw/*55.30*/("""{"""),format.raw/*55.31*/("""
        margin-bottom: 10px;
      """),format.raw/*57.7*/("""}"""),format.raw/*57.8*/("""
      .form-signin input[type="text"],
      .form-signin input[type="password"] """),format.raw/*59.43*/("""{"""),format.raw/*59.44*/("""
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      """),format.raw/*64.7*/("""}"""),format.raw/*64.8*/("""

    </style>
    </head>
    <body>
      <div class="container">
        """),_display_(Seq[Any](/*70.10*/helper/*70.16*/.form(routes.Application.authenticate, 'class -> "form-signin")/*70.79*/ {_display_(Seq[Any](format.raw/*70.81*/("""
            
            <h2 class="form-signin-heading">Please sign in</h2>
            
            """),_display_(Seq[Any](/*74.14*/form/*74.18*/.globalError.map/*74.34*/ { error =>_display_(Seq[Any](format.raw/*74.45*/("""
                <p class="error">
                    """),_display_(Seq[Any](/*76.22*/error/*76.27*/.message)),format.raw/*76.35*/("""
                </p>
            """)))})),format.raw/*78.14*/("""
            
            """),_display_(Seq[Any](/*80.14*/flash/*80.19*/.get("success").map/*80.38*/ { message =>_display_(Seq[Any](format.raw/*80.51*/("""
                <p class="success">
                    """),_display_(Seq[Any](/*82.22*/message)),format.raw/*82.29*/("""
                </p>
            """)))})),format.raw/*84.14*/("""
            
<!--             <p>
                <input type="text" class="input-block-level" name="conn" placeholder="Connection Name" id="conn" value=""""),_display_(Seq[Any](/*87.122*/form("conn")/*87.134*/.value)),format.raw/*87.140*/("""">
            </p>
-->       
           	<p>
				<input class="input-block-level" name="conn" placeholder="Connection" id="conn"/>
			</p>
            <p>
                <input type="password" class="input-block-level" name="password" id="password" placeholder="Password">
            </p>
            <p>
                <button class="btn btn-large btn-primary" type="submit" id="loginbutton">Login</button>
            </p>
            <p>
            <div id="report_pre">
            </div>
            
        """)))})),format.raw/*103.10*/("""
       </div>
       
       <script>
         $(function() """),format.raw/*107.23*/("""{"""),format.raw/*107.24*/("""
        	 	var ds = new kendo.data.DataSource("""),format.raw/*108.47*/("""{"""),format.raw/*108.48*/("""
        		  transport: """),format.raw/*109.24*/("""{"""),format.raw/*109.25*/("""
        		    read: """),format.raw/*110.21*/("""{"""),format.raw/*110.22*/("""
        		      url: """"),_display_(Seq[Any](/*111.24*/routes/*111.30*/.Assets.at("startupData/databases.json"))),format.raw/*111.70*/("""",
        		      dataType: "json",
        		      success : function(result) """),format.raw/*113.44*/("""{"""),format.raw/*113.45*/("""
        		    	 $("#report_pre").html(JSON.stringify(result));
        		      """),format.raw/*115.17*/("""}"""),format.raw/*115.18*/(""",
        		      error: function (xhr, error) """),format.raw/*116.46*/("""{"""),format.raw/*116.47*/("""
        		    	  console.debug(xhr); console.debug(error);
        		    	  alert("error");
        		      """),format.raw/*119.17*/("""}"""),format.raw/*119.18*/("""
        		    """),format.raw/*120.15*/("""}"""),format.raw/*120.16*/("""
        		  """),format.raw/*121.13*/("""}"""),format.raw/*121.14*/("""
        		"""),format.raw/*122.11*/("""}"""),format.raw/*122.12*/(""");
        	 	
     		
    			$("#conn").kendoComboBox("""),format.raw/*125.33*/("""{"""),format.raw/*125.34*/("""
    				dataTextField : "name",
    				dataValueField : "value",
    				index: 0,
    				dataSource : ds
    			"""),format.raw/*130.8*/("""}"""),format.raw/*130.9*/(""");
         """),format.raw/*131.10*/("""}"""),format.raw/*131.11*/(""");

       </script>
    </body>
</html>
    


"""))}
    }
    
    def render(form:Form[scala.Tuple2[String, String]],flash:Flash): play.api.templates.HtmlFormat.Appendable = apply(form)(flash)
    
    def f:((Form[scala.Tuple2[String, String]]) => (Flash) => play.api.templates.HtmlFormat.Appendable) = (form) => (flash) => apply(form)(flash)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Wed Jun 04 13:41:22 EDT 2014
                    SOURCE: /Users/BenGoodwin/Siena/amrApp/app/views/login.scala.html
                    HASH: afae5d132f40ce22a5086f470513f47b1a2bcff2
                    MATRIX: 590->1|736->53|809->91|823->97|889->142|1260->477|1275->483|1349->535|1423->573|1438->579|1514->633|1678->761|1693->767|1762->814|1834->850|1849->856|1915->900|1987->936|2002->942|2071->989|2143->1025|2158->1031|2228->1079|2300->1115|2315->1121|2392->1176|2513->1261|2528->1267|2598->1315|2675->1356|2690->1362|2748->1398|2812->1426|2827->1432|2888->1471|3011->1566|3040->1567|3166->1666|3194->1667|3243->1688|3272->1689|3734->2124|3762->2125|3861->2196|3890->2197|3953->2233|3981->2234|4091->2316|4120->2317|4256->2426|4284->2427|4397->2504|4412->2510|4484->2573|4524->2575|4664->2679|4677->2683|4702->2699|4751->2710|4843->2766|4857->2771|4887->2779|4954->2814|5017->2841|5031->2846|5059->2865|5110->2878|5204->2936|5233->2943|5300->2978|5493->3134|5515->3146|5544->3152|6098->3673|6188->3734|6218->3735|6294->3782|6324->3783|6377->3807|6407->3808|6457->3829|6487->3830|6548->3854|6564->3860|6627->3900|6736->3980|6766->3981|6875->4061|6905->4062|6981->4109|7011->4110|7149->4219|7179->4220|7223->4235|7253->4236|7295->4249|7325->4250|7365->4261|7395->4262|7479->4317|7509->4318|7653->4434|7682->4435|7723->4447|7753->4448
                    LINES: 19->1|22->1|26->5|26->5|26->5|36->15|36->15|36->15|37->16|37->16|37->16|42->21|42->21|42->21|43->22|43->22|43->22|44->23|44->23|44->23|45->24|45->24|45->24|46->25|46->25|46->25|50->29|50->29|50->29|51->30|51->30|51->30|52->31|52->31|52->31|56->35|56->35|60->39|60->39|62->41|62->41|74->53|74->53|76->55|76->55|78->57|78->57|80->59|80->59|85->64|85->64|91->70|91->70|91->70|91->70|95->74|95->74|95->74|95->74|97->76|97->76|97->76|99->78|101->80|101->80|101->80|101->80|103->82|103->82|105->84|108->87|108->87|108->87|124->103|128->107|128->107|129->108|129->108|130->109|130->109|131->110|131->110|132->111|132->111|132->111|134->113|134->113|136->115|136->115|137->116|137->116|140->119|140->119|141->120|141->120|142->121|142->121|143->122|143->122|146->125|146->125|151->130|151->130|152->131|152->131
                    -- GENERATED --
                */
            