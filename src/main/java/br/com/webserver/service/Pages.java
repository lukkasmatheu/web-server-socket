package main.java.br.com.webserver.service;

import java.util.Arrays;

public enum Pages {
    INDEX("/src/main/java/br/com/webserver/views/index.html","index.html"),
    ABOUT("/src/main/java/br/com/webserver/views/about.html","about.html"),
    FAVICON("/src/main/java/br/com/webserver/views/images/favicon.ico","favicon"),
    PNG("/src/main/java/br/com/webserver/views/images/image.png","image.png"),
    JPG("/src/main/java/br/com/webserver/views/images/image.jpg","image.jpg"),
    JPEG("/src/main/java/br/com/webserver/views/images/images.jpeg","image.jpeg"),
    IMAGE("/src/main/java/br/com/webserver/views/images/favicon-16x16.png","image"),
    ERROR("/src/main/java/br/com/webserver/views/error.html","error.html");

    String pathName;
    String namePage;

    Pages(String pathName, String namePage) {
        this.pathName = pathName;
        this.namePage = namePage;
    }
   public static String getPathIfPageExist(String releaseName){
        if(releaseName == null || releaseName.equals("/") || releaseName.trim().equals("")){
            return INDEX.pathName;
        }
       Pages enumFind =  Arrays.stream(Pages.values()).filter(viewPage-> viewPage.namePage.toLowerCase().equals(releaseName.replace("/",""))).findFirst().orElse(ERROR);

       return enumFind.pathName;
    }
}
