data class Location(val id: String, var name: String, var lat: Double, var lng: Double){
    init {
        println("Oject with  the name: ${name} created")
    }
}
