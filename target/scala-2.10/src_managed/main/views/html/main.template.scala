
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
object main extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template1[Html,play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply/*1.2*/(content: Html):play.api.templates.HtmlFormat.Appendable = {
        _display_ {

Seq[Any](format.raw/*1.17*/("""
<html>
    <head>

<!-- ================================== -->
<!-- twitter bootstrap -->
        <meta charset="utf-8">
    	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    	<meta name="viewport" content="width=device-width, initial-scale=1">
    	<title>Bootstrap 101 Template</title>

    	<link href=""""),_display_(Seq[Any](/*12.19*/routes/*12.25*/.Assets.at("bootstrap-3.1.1-dist/css/bootstrap.min.css"))),format.raw/*12.81*/("""" rel="stylesheet">

    	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    	<!--[if lt IE 9]>
      		<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      		<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    	<![endif]-->
<!-- ================================== -->
<!-- Kendo ui -->    
		<!-- Common Kendo UI Web CSS -->
		<link href=""""),_display_(Seq[Any](/*23.16*/routes/*23.22*/.Assets.at("kendo/styles/kendo.common.min.css"))),format.raw/*23.69*/("""" rel="stylesheet" />
		<!-- Default Kendo UI Web theme CSS -->
		<link href=""""),_display_(Seq[Any](/*25.16*/routes/*25.22*/.Assets.at("kendo/styles/kendo.default.min.css"))),format.raw/*25.70*/("""" rel="stylesheet" />
<!-- ================================== -->
    	<style>
    		body """),format.raw/*28.12*/("""{"""),format.raw/*28.13*/("""
  				padding-top: 50px;
			"""),format.raw/*30.4*/("""}"""),format.raw/*30.5*/("""
			.starter-template """),format.raw/*31.22*/("""{"""),format.raw/*31.23*/("""
  				padding: 40px 15px;
  				text-align: center;
			"""),format.raw/*34.4*/("""}"""),format.raw/*34.5*/("""
		</style>
  </head>
  <body>    
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">AMR Configuration</a>
        </div>
        <div class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="#about">About</a></li>
            <li><a href="#contact">Contact</a></li>
          </ul>
          <ul class="nav navbar-nav pull-right">
          	<li><a href="logout">logout</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>
        
    <div class="container">
    	<div class="starter-template">
    		<h1>Bootstrap starter template</h1>
        	<p class="lead">"""),_display_(Seq[Any](/*65.27*/content)),format.raw/*65.34*/("""</p>
		</div>
	</div>
	
	<!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src=""""),_display_(Seq[Any](/*72.19*/routes/*72.25*/.Assets.at("javascripts/jquery-1.11.1.min.js"))),format.raw/*72.71*/(""""></script>
    <script src=""""),_display_(Seq[Any](/*73.19*/routes/*73.25*/.Assets.at("bootstrap-3.1.1-dist/js/bootstrap.min.js"))),format.raw/*73.79*/(""""></script>
	<script src=""""),_display_(Seq[Any](/*74.16*/routes/*74.22*/.Assets.at("kendo/js/kendo.web.min.js"))),format.raw/*74.61*/(""""></script>	
  </body>
</html>
"""))}
    }
    
    def render(content:Html): play.api.templates.HtmlFormat.Appendable = apply(content)
    
    def f:((Html) => play.api.templates.HtmlFormat.Appendable) = (content) => apply(content)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Wed Jun 04 11:14:07 EDT 2014
                    SOURCE: /Users/BenGoodwin/Siena/amrApp/app/views/main.scala.html
                    HASH: 26c35db2024bc7d2136e4381a7b2b10ef439eb0a
                    MATRIX: 553->1|662->16|1015->333|1030->339|1108->395|1671->922|1686->928|1755->975|1870->1054|1885->1060|1955->1108|2073->1198|2102->1199|2158->1228|2186->1229|2236->1251|2265->1252|2348->1308|2376->1309|3558->2455|3587->2462|3828->2667|3843->2673|3911->2719|3977->2749|3992->2755|4068->2809|4131->2836|4146->2842|4207->2881
                    LINES: 19->1|22->1|33->12|33->12|33->12|44->23|44->23|44->23|46->25|46->25|46->25|49->28|49->28|51->30|51->30|52->31|52->31|55->34|55->34|86->65|86->65|93->72|93->72|93->72|94->73|94->73|94->73|95->74|95->74|95->74
                    -- GENERATED --
                */
            