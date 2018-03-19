##  Pathfinder || Android App 

Pathfinder, gezgin satıcı problemini çözümlerken genetik algoritma(Php) kullanarak, optimum mesafeyi hesaplayarak başladığı noktaya dönmeyi
hedefler. Bu sayede yoldan, zamandan ve yakıt vb. maliyetlerden kazanç sağlamaktadır. Lojistik, kargo, paket taşıma gibi iş alanları 
düşünülerek tasarlanmıştır. 



##  Proje Kullanım

Uygulamayı indiğinde Android Studio ile açın.
Projeyi import edin.
Kendinize Google Maps API Key alın ve uygulamaya ekleyin.



##  Uygulama Kullanım

Eklemek istenilen lokasyonlar girilir, lokasyonlar için veritabanı gerekmektedir. Uygulama geliştirilme sürecinde **MySQL** kullanılmıştır.
Veritabanında tutulan lokasyonlar ve mesafeler, rota çıkarma işlemine geçildiğinde Genetik Algoritma uygulanarak hesaplanır ve uygulama üzerinde Google Maps ekranında rota çizilir.

Koordinatlar ve aralarındaki mesafeler **Google Maps Directions API** kullanılarak hesaplanmıştır. Genetik algoritma **Mustafa Gemicio** tarafından **PHP** ile kodlanmıştır linkden ulaşabilirsiniz.

https://github.com/Gemside/TSP-solving-with-Genetic-alg

##  Veritabanı İşlemleri Scriptleri
Scriptler veritabanı ile uygulama arasında işlemler için kullanılmıştır. Kullanımı için uygulama içinde script yolunun gösterilmesi yeterlidir.

[connection.php](http://s000.tinyupload.com/download.php?file_id=02655489293542626831&t=0265548929354262683138299)
[insertlocation.php](http://s000.tinyupload.com/download.php?file_id=64512845122083336705&t=6451284512208333670576456)
[insertdistance.php](http://s000.tinyupload.com/download.php?file_id=09245574105673796651&t=0924557410567379665151965)
[showlocation.php](http://s000.tinyupload.com/download.php?file_id=02478042570396463714&t=0247804257039646371474851)
[showlocation2.php](http://s000.tinyupload.com/download.php?file_id=98475889323652126694&t=9847588932365212669461679)
[truncate.php](http://s000.tinyupload.com/download.php?file_id=91118462329096155342&t=9111846232909615534209791)



## Uygulama Görüntüleri
![1](https://user-images.githubusercontent.com/30620969/37623814-01159116-2bd7-11e8-92f1-5e139be65ccb.jpg)     ![2](https://user-images.githubusercontent.com/30620969/37623883-451393cc-2bd7-11e8-8f30-7be2bb6c14c9.jpg)     ![3](https://user-images.githubusercontent.com/30620969/37623890-47045e50-2bd7-11e8-98e3-599df0c56b3b.jpg)     ![4](https://user-images.githubusercontent.com/30620969/37623893-4c9ab940-2bd7-11e8-9a71-eac675321e46.jpg)
