package com.ge.digital.webcrawler;

//Page POJO class
class Page {
    private String address;
    private String[] links;

    Page() {
    	
    }

    final String getAddress() {
        return this.address;
    }
    
    final void setAddress(String address) {
        this.address = address;
    }

    final String[] getLinks() {
        return this.links;
    }

    final void setLinks(String[] links) {
        this.links = links;
    }
}
