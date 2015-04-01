package de.appdynamics.ace.apdex.util.api;

/**
 * Created by stefan.marx on 18.12.14.
 */
public class QueryConstants {
    public static final String SINGLE_PAGE_BANDS = "{\n" +
            "  \"size\": 0,\n" +
            "  \"query\" : {\n" +
            "    \"filtered\" : {\n" +
            "      \"query\" : {\n" +
            "        \"match_all\" : { }\n" +
            "      },\n" +
            "      \n" +
            "      \"filter\" : {\n" +
            "        \"bool\" : {\n" +
            "          \"must\" : [ {\n" +
            "            \"match_all\" : { }\n" +
            "          }, {\n" +
            "            \"term\" : {\n" +
            "              \"pagename\" : \"::PAGE::\"\n" +
            "            }\n" +
            "          },\n" +
            "\n" +
            "          {\n" +
            "            \"range\" : {\n" +
            "              \"eventTimestamp\" : {\n" +
            "                \"gt\" : ::FROM::,\n" +
            "                \"lt\" : ::TO::\n" +
            "              }\n" +
            "          }\n" +
            "        }\n" +
            "          ,\n" +
            "          {\n" +
            "            \"range\" : {\n" +
            "              \"metrics.enduserresponsetime\" : {\n" +
            "                \"gt\" : \"0\"" +
            "              }\n" +
            "          }\n" +
            "        } " +
            "           ]\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "    \"aggs\" : {\n" +
            "        \"page\" : {\n" +
            "            \"terms\" : { " +
            "                \"size\":0,\n" +
            "                \"field\" : \"pagename\"\n" +
            "                \n" +
            "            },\n" +
            "            \"aggs\" : {\n" +
            "\t           \"bands\": {\n" +
            "\"range\" : {\n" +
            "                \"field\" : \"metrics.enduserresponsetime\",\n" +
            "                \"ranges\" : [\n" +
            "                    { \"to\" : ::B1:: },\n" +
            "                    { \"from\" : ::B1::, \"to\" : ::B2:: },\n" +
            "                    { \"from\" : ::B2:: }\n" +
            "                ]\n" +
            "            }                    \n" +
            "                }\n" +
            " \t\t\t}\n" +
            "        }\n" +
            "    }\n" +
            "}";
    public static final String ALL_PAGE_BANDS = "{\n" +
            "  \"size\": 0,\n" +
            "  \"query\" : {\n" +
            "    \"filtered\" : {\n" +
            "      \"query\" : {\n" +
            "        \"match_all\" : { }\n" +
            "      },\n" +
            "      \n" +
            "      \"filter\" : {\n" +
            "        \"bool\" : {\n" +
            "          \"must\" : [ {\n" +
            "            \"match_all\" : { }\n" +
            "          },\n" +
            "\n" +
            "          {\n" +
            "            \"range\" : {\n" +
            "              \"eventTimestamp\" : {\n" +
            "                \"gt\" : ::FROM::,\n" +
            "                \"lt\" : ::TO::\n" +
            "              }\n" +
            "          }\n" +
            "        }\n" +
            "          ,\n" +
            "          {\n" +
            "            \"range\" : {\n" +
            "              \"metrics.enduserresponsetime\" : {\n" +
            "                \"gt\" : \"0\"" +
            "              }\n" +
            "          }\n" +
            "        } " +
            "           ]\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "    \"aggs\" : {\n" +
            "        \"page\" : {\n" +
            "            \"terms\" : {\n" +
            "                \"field\" : \"pagename\",\n" +
            "                \"size\": 0\n" +
            "            },\n" +
            "            \"aggs\" : {\n" +
            "\t           \"bands\": {\n" +
            "\"range\" : {\n" +
            "                \"field\" : \"metrics.enduserresponsetime\",\n" +
            "                \"ranges\" : [\n" +
            "                    { \"to\" : ::B1:: },\n" +
            "                    { \"from\" : ::B1::, \"to\" : ::B2:: },\n" +
            "                    { \"from\" : ::B2:: }\n" +
            "                ]\n" +
            "            }                    \n" +
            "                }\n" +
            " \t\t\t}\n" +
            "        }\n" +
            "    }\n" +
            "}";
    public static final String SINGLE_PAGE_BANDS_WITH_APP = "{\n" +
            "  \"size\": 0,\n" +
            "  \"query\" : {\n" +
            "    \"filtered\" : {\n" +
            "      \"query\" : {\n" +
            "        \"match_all\" : { }\n" +
            "      },\n" +
            "      \n" +
            "      \"filter\" : {\n" +
            "        \"bool\" : {\n" +
            "          \"must\" : [ {\n" +
            "            \"match_all\" : { }\n" +
            "          }, {\n" +
            "            \"term\" : {\n" +
            "              \"pagename\" : \"::PAGE::\"\n" +
            "            }\n" +
            "          },{\n" +
            "            \"term\": {\n" +
            "                \"appkey\": \"::APP::\"\n" +
            "              }\n" +
            "            }," +
            "\n" +
            "          {\n" +
            "            \"range\" : {\n" +
            "              \"eventTimestamp\" : {\n" +
            "                \"gt\" : ::FROM::,\n" +
            "                \"lt\" : ::TO::\n" +
            "              }\n" +
            "          }\n" +
            "        }\n" +
            "          ,\n" +
            "          {\n" +
            "            \"range\" : {\n" +
            "              \"metrics.enduserresponsetime\" : {\n" +
            "                \"gt\" : \"0\"" +
            "              }\n" +
            "          }\n" +
            "        } " +
            "           ]\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "    \"aggs\" : {\n" +
            "        \"page\" : {\n" +
            "            \"terms\" : {" +
            "                \"size\":0,\n" +
            "                \"field\" : \"pagename\"\n" +
            "                \n" +
            "            },\n" +
            "            \"aggs\" : {\n" +
            "\t           \"bands\": {\n" +
            "\"range\" : {\n" +
            "                \"field\" : \"metrics.enduserresponsetime\",\n" +
            "                \"ranges\" : [\n" +
            "                    { \"to\" : ::B1:: },\n" +
            "                    { \"from\" : ::B1::, \"to\" : ::B2:: },\n" +
            "                    { \"from\" : ::B2:: }\n" +
            "                ]\n" +
            "            }                    \n" +
            "                }\n" +
            " \t\t\t}\n" +
            "        }\n" +
            "    }\n" +
            "}";
    public static final String MULTIPLE_PAGE_BANDS_WITH_APP = "{\n" +
            "  \"size\": 0,\n" +
            "  \"query\" : {\n" +
            "    \"filtered\" : {\n" +
            "      \"query\" : {\n" +
            "        \"match_all\" : { }\n" +
            "      },\n" +
            "      \n" +
            "      \"filter\" : {\n" +
            "        \"bool\" : {\n" +
            "          \"must\" : [ {\n" +
            "            \"match_all\" : { }\n" +
            "          }, {\n" +
            "            \"terms\" : {\n" +
            "              \"pagename\" : ::PAGE::\n" +
            "            }\n" +
            "          },{\n" +
            "            \"term\": {\n" +
            "                \"appkey\": \"::APP::\"\n" +
            "              }\n" +
            "            }," +
            "\n" +
            "          {\n" +
            "            \"range\" : {\n" +
            "              \"eventTimestamp\" : {\n" +
            "                \"gt\" : ::FROM::,\n" +
            "                \"lt\" : ::TO::\n" +
            "              }\n" +
            "          }\n" +
            "        }\n" +
            "          ,\n" +
            "          {\n" +
            "            \"range\" : {\n" +
            "              \"metrics.::METRICFIELD::\" : {\n" +
            "                \"gt\" : \"0\"" +
            "              }\n" +
            "          }\n" +
            "        } " +
            "           ]\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "    \"aggs\" : {\n" +
            "        \"page\" : {\n" +
            "            \"terms\" : {\n" +
            "                               \"size\":0," +
            "                \"field\" : \"pagename\"\n" +
            "                \n" +
            "            },\n" +
            "            \"aggs\" : {\n" +
            "\t           \"bands\": {\n" +
            "\"range\" : {\n" +
            "                \"field\" : \"metrics.::METRICFIELD::\",\n" +
            "                \"ranges\" : [\n" +
            "                    { \"to\" : ::B1:: },\n" +
            "                    { \"from\" : ::B1::, \"to\" : ::B2:: },\n" +
            "                    { \"from\" : ::B2:: }\n" +
            "                ]\n" +
            "            }                    \n" +
            "                }\n" +
            " \t\t\t}\n" +
            "        }\n" +
            "    }\n" +
            "}";

    public static final String ALL_PAGE_BANDS_COMBINED = "{\n" +
            "  \"size\": 0,\n" +
            "  \"query\" : {\n" +
            "    \"filtered\" : {\n" +
            "      \"query\" : {\n" +
            "        \"match_all\" : { }\n" +
            "      },\n" +
            "      \n" +
            "      \"filter\" : {\n" +
            "        \"bool\" : {\n" +
            "          \"must\" : [ {\n" +
            "            \"match_all\" : { }\n" +
            "          }," +
            "           {\n" +
            "                \"term\" : {\n" +
            "                    \"pagetype\":\"BASE_PAGE\"\n" +
            "                }    \n" +
            "            }," +
            "          {\n" +
            "            \"range\" : {\n" +
            "              \"eventTimestamp\" : {\n" +
            "                \"gt\" : ::FROM::,\n" +
            "                \"lt\" : ::TO::\n" +
            "              }\n" +
            "          }\n" +
            "        }\n" +
            "          ,\n" +
            "          {\n" +
            "            \"range\" : {\n" +
            "              \"metrics.::METRICFIELD::\" : {\n" +
            "                \"gt\" : \"0\"" +
            "              }\n" +
            "          }\n" +
            "        } " +
            "           ]\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "    \"aggs\" : {\n" +
            "\t           \"bands\": {\n" +
            "\"range\" : {\n" +
            "                \"field\" : \"metrics.::METRICFIELD::\",\n" +
            "                \"ranges\" : [\n" +
            "                    { \"to\" : ::B1:: },\n" +
            "                    { \"from\" : ::B1::, \"to\" : ::B2:: },\n" +
            "                    { \"from\" : ::B2:: }\n" +
            "                ]\n" +
            "            }                    \n" +
            "                }\n" +
            "    }\n" +
            "}";
}
