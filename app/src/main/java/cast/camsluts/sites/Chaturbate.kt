package cast.camsluts.sites

import android.util.Log
import cast.camsluts.models.Model
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Chaturbate {

    fun parseVideo(url:String):String{
        val html:String = jsoupToHtml(url)
        val rexx = """http(s)?://([\w+?\.\w+])+([a-zA-Z0-9\~\!\@\#\$\%\^\&\*\(\)_\-\=\+\\\/\?\.\:\;\'\,]*)?.m3u8""".toRegex()
        val finds = rexx.findAll(html)
        finds.forEach {
            var rep: String = it.groupValues[0]
            rep = rep.replace("\\u0027", "'").replace("\\u0022", "\"").replace("\\u002D", "-").replace("\\u005C", "\"").replace("u003D", "=")
          //  Log.d("", rep)
            return rep
        }
        return ""
    }

    fun parseCams(url:String):MutableList<Model>{
        val listData = mutableListOf<Model>()
        val doc: Document = jsoupToDocument(url)
        val tagName = doc.tagName("li")
        for (tag in tagName.getElementsByClass("room_list_room")){
            val name:String = tag.getElementsByClass("no_select").attr("href").replace("/", "")
            val image:String = tag.getElementsByClass("png").attr("src")
            val age:String = isINT(tag.getElementsByClass("age").text())
            val label:String = tag.getElementsByClass("thumbnail_label").text()
            val views:String = tag.getElementsByClass("cams").text()
            val descriptions:String = tag.getElementsByClass("subject").text()
            val link:String = "https://chaturbate.com" + tag.getElementsByClass("no_select").attr("href")
            // if (label.isNotEmpty()){
            listData.add(Model(name,image,label,age,descriptions,views,link))
           // }else {
           //     listData.add(Model(name,image,"",age,descriptions,views,link))
           // }
            //  print(image.html())
//label.ifEmpty { "" }
            Log.d("", name)
        }
        return listData
    }

    private fun isINT(value:String):String{
        val check = "[0-9]+".toRegex()
        return if(value.matches(check)){ value } else{""}
    }

    private fun jsoupToHtml(Url:String): String{
        var html:String? = null
        try{
            html = Jsoup.connect(Url).get().html()
        } catch (e: Exception) {
            Log.e("Voyeurism ", "Error parsing " + e.message, e)
        }
        return html!!
    }
    private fun jsoupToDocument(Url:String): Document {
        var doc: Document? = Document(Url)  //= Document(Url)
        try{
            doc = Jsoup.connect(Url).get()
        } catch (e: Exception) {
            Log.e("Voyeurism ", "Error parsing " + e.message, e)
        }
        return doc!!
    }

}