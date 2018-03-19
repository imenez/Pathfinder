##  Android Uygulama : # Pathfinder

Pathfinder, gezgin satıcı problemini çözümlerken genetik algoritma(Php) kullanarak, optimum mesafeyi hesaplayarak başladığı noktaya dönmeyi
hedefler. Bu sayede yoldan, zamandan ve yakıt vb. maliyetlerden kazanç sağlamaktadır. Lojistik, kargo, paket taşıma gibi iş alanları 
düşünülerek tasarlanmıştır. 



##  Proje Kullanım

Uygulamayı indiğinde Android Studio ile açın.
Projeyi import edin.
Kendinize Google Maps API Key alın ve uygulamaya ekleyin.


##  Uygulama Kullanım

Eklemek istenilen lokasyonlar girilir, lokasyonlar için veritabanı gerekmektedir. Uygulama geliştirilme sürecinde MySQL kullanılmıştır.
Veritabanında tutulan lokasyonlar ve mesafeler, rota çıkarma işlemine geçildiğinde Genetik Algoritma uygulanarak hesaplanır ve uygulama üzerinde Google Maps ekranında rota çizilir.

Koordinatlar ve aralarındaki mesafeler Google Maps Directions API kullanılarak hesaplanmıştır. Genetik algoritma Mustafa Gemicio tarafından php ile kodlanmıştır linkden ulaşabilirsiniz.

https://github.com/Gemside/TSP-solving-with-Genetic-alg
