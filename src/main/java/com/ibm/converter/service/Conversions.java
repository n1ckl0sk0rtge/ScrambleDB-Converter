package com.ibm.converter.service;

import java.util.*;
import com.ibm.jgroupsig.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Conversions {

    @Inject
    CacheRepository cacheRepository;

    private final String bldPubKey =
            "AwMwAAAAOJy2Da/rC6iJr6Yle/FOVsqf5IRU8X0BnWv65QEHD+zPzLU+o90BMSv60+EbhvmJAAAAAA==";
    private final String grpKey =
            "AwAwAAAAkY4aevm4rC2Gf/OKQ5q+0FHoq+77KJ+nEVX4CidY/LMp+g2nHIqMzRa/vWtz+B4ZYAAA" +
            "AICzmyuvL/SmsNZ1b4IY/wcJ1MasBOtpf/Aus7Jg73b4grJFaqu6nG4TfA2OR2DLBZOu28Dmzga2" +
            "w9HHoDBYnJZB1X9oIlRF8Q1nbrAZNXtQb19UahAdhoKcFyCBfJbblTAAAADceh8hWyORaiusnn62" +
            "B1tBkhLTpJfofZLTG3D+3uGN5WCmC8htEeYieAjVYHW3TZUwAAAAbwOIL5CYTfluKgYAnbWSN1XQ" +
            "FmC/KqwCdb5ZXH+Jjslz8rqUWzNjZJLOCrhvy9OUMAAAAFc+/z8u1MjkKr39BfHFMwdnJ2pY0poh" +
            "DGth86aR774lkEgFA6cQo/4T/76aWb8xCzAAAADYD3teB08gV11HDvUdEjRrBM/e2H4k0PjWfGKL" +
            "iiv5VNL59eoklNIXtiWVmic8m5YwAAAAH8F1ZdS0tLcdQ6Tks1rAw/EvFyU1n9vPg7Ixy0ZZeMiK" +
            "hr2fiAhdEPu51h9P9uUZYAAAACHrlGU5AGvkxmXZg0QjQfF/cnQvHHpeP22L0+pos/61CcDYVbkT" +
            "XR9UsHI/U4iQFKP4z1wHwatzUWoyS2MnH+XgwRsJ8PAVa6dcc56Qiptw6g6pWR4loHMniQzyHoZP" +
            "GTAAAACyiH8hTL5E69uko+kDFvLiJzt16Sn+j61n74H0Sl5DrACyfhocgBaWx41OwHgxxRgwAAAA" +
            "NalzJL+TAQea0CWs4ZnfUt/6LsF9un0HIKoE0zOGi4k/EvLGf4UeqMcannuburmZ";
    private final String cnvKey =
            "AwEAAAAAIAAAACEJnWEnuF8saHqNIa5Hg8iqnS5/fiz3exbZ72/tXgASIAAAAGIAHHsRw2EU8JyK" +
            "wQEhaTVQ/X8oolm+ird60HnuLNQ0";

    Conversions() {}

    public Dictionary<String,String> convert(List<String> pseudonyms) {

        try {
            Dictionary<String, String> results = new Hashtable<>();

            for (String p : pseudonyms) {
                String cachedValue = cacheRepository.get(p);
                if (!(cachedValue.equals(""))) {
                    results.put(p, cachedValue);
                } else {
                    //TODO Crypto
                    GL19 converter = new GL19();
                    GrpKey groupKey = new GrpKey(GS.GL19_CODE, grpKey);
                    converter.setup(groupKey);
                    results.put(p, "converted");
                    cacheRepository.set(p, "converted");
                }
            }

            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
