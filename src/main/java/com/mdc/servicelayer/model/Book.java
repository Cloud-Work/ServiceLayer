package com.mdc.servicelayer.model;

import javax.persistence.*;

@Entity
@Table(name = "book", schema = "mdc")
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String author;

    @Column(name = "imageurl")
    private String imageUrl;

    @Column(name = "purchaseurl")
    private String purchaseUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPurchaseUrl() {
        return purchaseUrl;
    }

    public void setPurchaseUrl(String purchaseUrl) {
        this.purchaseUrl = purchaseUrl;
    }

}
