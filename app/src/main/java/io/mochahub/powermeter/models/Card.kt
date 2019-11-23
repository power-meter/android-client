package io.mochahub.powermeter.models


data class Card(

    var title: String,

    var body: String,

    var image: Int


) {

    override fun toString(): String {
        return "BlogPost(title='$title', image='$image')"
    }


}
