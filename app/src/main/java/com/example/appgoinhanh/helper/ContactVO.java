package com.example.appgoinhanh.helper;

import android.graphics.Bitmap;
import android.net.Uri;

public class ContactVO {

    private String ContactId;
    private Bitmap ContactImage;
    private String ContactName;
    private String ContactNumber;
    private Uri PhotoURI;

    public ContactVO() {
    }

    public ContactVO(String contactId, Bitmap contactImage, String contactName, String contactNumber, Uri uri) {
        this.ContactId = contactId;
        this.ContactImage = contactImage;
        this.ContactName = contactName;
        this.ContactNumber = contactNumber;
        this.PhotoURI = uri;
    }

    public String getContactId() {
        return ContactId;
    }

    public void setContactId(String contactId) {
        this.ContactId = contactId;
    }

    public Bitmap getContactImage() {
        return ContactImage;
    }

    public void setContactImage(Bitmap contactImage) {
        this.ContactImage = contactImage;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        this.ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.ContactNumber = contactNumber;
    }

    public Uri getPhotoURI() {
        return PhotoURI;
    }

    public void setPhotoURI(Uri photoURI) {
        this.PhotoURI = photoURI;
    }
}
