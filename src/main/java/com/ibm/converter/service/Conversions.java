package com.ibm.converter.service;

import java.util.*;
import com.ibm.jgroupsig.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Conversions {

    private static final String GRPKEY =
            "AwAwAAAAbpYmcOEapyBIWn55+VMnIu8RgbUB4+qOxwruMO2izjU1Xhk5RJIhmb9j4HpAS32ZYAAA\n" +
            "ADme3GywvNHxmOj2Ec2x23rqPGMX+gU14yuM/WqTPis2b66DdmcH+h9yHxAneOUUGDQcZCSzPNlq\n" +
            "7Nba3Bp/8avK23V/fXbO+LU1bMkH6K1KOJcL3MD3yrKezykv2oRgmTAAAACUnMUdYtck4dg6kg/S\n" +
            "keybnG0BrC2Y+iZ1ZS2GVDMp94z3PjapltvIfjqR8B6yXgUwAAAAPPzAVL3JiLRoXdwuhh9+uFFy\n" +
            "qaNU0sswuiJyGmnJjGLlCiSodF6NQa1kdcUS3ySTMAAAAP1067s7icCrHdcAB5a27Je+0Qe2BwRL\n" +
            "wQfwZ8cI8B4CH3fhhtvhshxrSbZwTeqBiTAAAACfvkEuUOVW7t9DrTsM2NMz+A86OgzLs3LyiJKz\n" +
            "2co+GHCSc2aOxIl3PDcUXFKZiRAwAAAAcw3PI87ZzooEhGrg3d5g/k6s1PoZuuLxGiyblBDp0+AG\n" +
            "QiDFIlHuN48LkaBEZWwGYAAAAIxIOfVcAeBsiY6MbBVRSOaZBZy+2o6Dq/jd/0QDE838D39lZPGa\n" +
            "nRxpb2wLpfFrCQzNoTQ8cuHEBQX6V7gjIIJkPymgYkWC2RoKrRTPwKXBGamiDyHkJsJ/WyNTKceW\n" +
            "FTAAAADx7HRfBQSDp4oJtiCK6kCAs8cdZ6tTtrUsTr6ACjl2AVeCtxl9lFx47G8KM/WIOhUwAAAA\n" +
            "5IQOJ2qNbpie76hr+Ijhqd51R+UbV/jHdFgCRfBmqsEpcMv2qw2cUbmHWkdg5ZgN";
    private static final String BLDPUBKEY =
            "AwMwAAAAnSePXpmLifJZmZ96mAwNgVGFb2oBaQffRo+TcTjoiXi5CgR2rk/UqwA8S0HUCGAYAAAA\n" +
            "AA==";

    @Inject
    CacheRepository cacheRepository;

    Conversions() {}

    public Dictionary<String,String> convert(List<String> pseudonyms) {

        try {
            GL19 converter = new GL19();
            GrpKey grpKey = new GrpKey(GS.GL19_CODE, GRPKEY);
            BldKey bldKey = new BldKey(GS.GL19_CODE, BLDPUBKEY);
            converter.setup(grpKey);

            Dictionary<String, String> results = new Hashtable<>();

            for (String p : pseudonyms) {
                String cachedValue = cacheRepository.get(p);
                if (!(cachedValue.equals(""))) {
                    results.put(p, cachedValue);
                } else {
                    BlindSignature pseudonym = new BlindSignature(GS.GL19_CODE, p);

                    BlindSignature[] result = converter.convert( new BlindSignature[]{pseudonym}, bldKey);
                    String value = result[0].export();

                    results.put(p, value);
                    cacheRepository.set(p, value);
                }
            }
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
