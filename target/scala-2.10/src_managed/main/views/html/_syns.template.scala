
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
object _syns extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template0[play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply/*1.2*/():play.api.templates.HtmlFormat.Appendable = {
        _display_ {

Seq[Any](format.raw/*1.4*/("""
<div id="syn" class="groupInner">
	<h4>Astorm Synonyms</h4>
	<input id="astormSynonym" style="width: 150px" />
	<h4>Siena Synonyms</h4>
	<input id="sienaSynonym" style="width: 150px" />
</div>

<script>
	var synonyms = [ """),format.raw/*10.19*/("""{"""),format.raw/*10.20*/("""
		"astormSyn" : """),format.raw/*11.17*/("""{"""),format.raw/*11.18*/("""
			"values" : [ """),format.raw/*12.17*/("""{"""),format.raw/*12.18*/("""
				"text" : "astorm2",
				"value" : 0,
				"set" : true
			"""),format.raw/*16.4*/("""}"""),format.raw/*16.5*/(""", """),format.raw/*16.7*/("""{"""),format.raw/*16.8*/("""
				"text" : "dms_a",
				"value" : 1
			"""),format.raw/*19.4*/("""}"""),format.raw/*19.5*/(""" ]
		"""),format.raw/*20.3*/("""}"""),format.raw/*20.4*/(""",
		"sienaSyn" : """),format.raw/*21.16*/("""{"""),format.raw/*21.17*/("""
			"values" : [ """),format.raw/*22.17*/("""{"""),format.raw/*22.18*/("""
				"text" : "siena",
				"value" : 0,
				"set" : true
			"""),format.raw/*26.4*/("""}"""),format.raw/*26.5*/(""", """),format.raw/*26.7*/("""{"""),format.raw/*26.8*/("""
				"text" : "siena_web_services",
				"value" : 1
			"""),format.raw/*29.4*/("""}"""),format.raw/*29.5*/(""" ]
		"""),format.raw/*30.3*/("""}"""),format.raw/*30.4*/("""
	"""),format.raw/*31.2*/("""}"""),format.raw/*31.3*/(""" ];

	//Build the data source for the items
	var dataSource = new kendo.data.DataSource("""),format.raw/*34.45*/("""{"""),format.raw/*34.46*/("""
		data : synonyms,
		change : function(e) """),format.raw/*36.24*/("""{"""),format.raw/*36.25*/("""
			renderConfig(e.items[0].astormSyn.values, "#astormSynonym");
			renderConfig(e.items[0].sienaSyn.values, "#sienaSynonym");
		"""),format.raw/*39.3*/("""}"""),format.raw/*39.4*/("""
	"""),format.raw/*40.2*/("""}"""),format.raw/*40.3*/(""");

	// Initial read of the items in to the data source
	dataSource.read();

	function renderConfig(values, id) """),format.raw/*45.36*/("""{"""),format.raw/*45.37*/("""
		// find the currently selected item
		var index;
		for (i = 0; i < syn.length; i++) """),format.raw/*48.36*/("""{"""),format.raw/*48.37*/("""
			if (values[i].set == true)
				index = values[i].value;
		"""),format.raw/*51.3*/("""}"""),format.raw/*51.4*/("""
		;

		// render the combobox
		$(id).kendoComboBox("""),format.raw/*55.23*/("""{"""),format.raw/*55.24*/("""
			dataTextField : "text",
			dataValueField : "value",
			dataSource : values,
			index : index,
			change : function(e) """),format.raw/*60.25*/("""{"""),format.raw/*60.26*/("""
				var combobox1 = $("#astormSynonym").data("kendoComboBox")
				var combobox2 = $("#sienaSynonym").data("kendoComboBox")
				console.log("values = " + combobox1.text() + ", " + combobox2.text());
			"""),format.raw/*64.4*/("""}"""),format.raw/*64.5*/("""
		"""),format.raw/*65.3*/("""}"""),format.raw/*65.4*/(""");
	"""),format.raw/*66.2*/("""}"""),format.raw/*66.3*/("""

	function renderSienaSynonym(syn) """),format.raw/*68.35*/("""{"""),format.raw/*68.36*/("""
	"""),format.raw/*69.2*/("""}"""),format.raw/*69.3*/("""
</script>"""))}
    }
    
    def render(): play.api.templates.HtmlFormat.Appendable = apply()
    
    def f:(() => play.api.templates.HtmlFormat.Appendable) = () => apply()
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Tue Jun 03 11:56:18 EDT 2014
                    SOURCE: /Users/BenGoodwin/Siena/amrApp/app/views/_syns.scala.html
                    HASH: 2132f00f2e5e4c22d9feb636c806e46fdc4bbed4
                    MATRIX: 549->1|644->3|894->225|923->226|968->243|997->244|1042->261|1071->262|1160->324|1188->325|1217->327|1245->328|1314->370|1342->371|1374->376|1402->377|1447->394|1476->395|1521->412|1550->413|1637->473|1665->474|1694->476|1722->477|1804->532|1832->533|1864->538|1892->539|1921->541|1949->542|2065->630|2094->631|2165->674|2194->675|2350->804|2378->805|2407->807|2435->808|2575->920|2604->921|2719->1008|2748->1009|2837->1071|2865->1072|2946->1125|2975->1126|3126->1249|3155->1250|3384->1452|3412->1453|3442->1456|3470->1457|3501->1461|3529->1462|3593->1498|3622->1499|3651->1501|3679->1502
                    LINES: 19->1|22->1|31->10|31->10|32->11|32->11|33->12|33->12|37->16|37->16|37->16|37->16|40->19|40->19|41->20|41->20|42->21|42->21|43->22|43->22|47->26|47->26|47->26|47->26|50->29|50->29|51->30|51->30|52->31|52->31|55->34|55->34|57->36|57->36|60->39|60->39|61->40|61->40|66->45|66->45|69->48|69->48|72->51|72->51|76->55|76->55|81->60|81->60|85->64|85->64|86->65|86->65|87->66|87->66|89->68|89->68|90->69|90->69
                    -- GENERATED --
                */
            