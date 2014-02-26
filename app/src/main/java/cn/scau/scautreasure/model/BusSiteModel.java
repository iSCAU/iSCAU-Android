package cn.scau.scautreasure.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 校巴站点信息模型;
 *
 * User: special
 * Date: 13-9-8
 * Time: 下午1:15
 * Mail: specialcyci@gmail.com
 */
public class BusSiteModel implements Serializable {

    private int    id;
    private String site;
    private String direction;
    private double longitude;
    private double latitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }


    public void setSite(String site) {
        this.site = site;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "BusSiteModel{" +
                "id=" + id +
                ", site='" + site + '\'' +
                ", direction='" + direction + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    public class SiteList{

        private ArrayList<BusSiteModel> sites;

        public ArrayList<BusSiteModel> getSites() {
            return sites;
        }

        public void setSites(ArrayList<BusSiteModel> sites) {
            this.sites = sites;
        }
    }

}
