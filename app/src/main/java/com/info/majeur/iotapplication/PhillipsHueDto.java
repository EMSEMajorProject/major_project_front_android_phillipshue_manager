package com.info.majeur.iotapplication;

public class PhillipsHueDto {
    private Boolean on;
    private Long sat;
    private Long bri;
    private Long hue;

    public PhillipsHueDto(Boolean on, Long sat, Long bri, Long hue) {
        this.sat = sat;
        this.bri = bri;
        this.on = on;
        this.hue = hue;
    }

    public Boolean getOn() {
        return on;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }

    public Long getSat() {
        return sat;
    }

    public void setSat(Long sat) {
        this.sat = sat;
    }

    public Long getBri() {
        return bri;
    }

    public void setBri(Long bri) {
        this.bri = bri;
    }

    public Long getHue() {
        return hue;
    }

    public void setHue(Long hue) {
        this.hue = hue;
    }
}

