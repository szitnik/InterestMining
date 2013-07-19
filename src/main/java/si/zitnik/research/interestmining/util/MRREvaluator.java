package si.zitnik.research.interestmining.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 7/6/13
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class MRREvaluator {


    public static Double calculateMRRRank(String questionId, ConcurrentHashMap<String, Double> result)
    {
        final ConcurrentHashMap<String, Double> result1 = result;
        TreeMap<String, Double> ordered = new TreeMap<String, Double>(new Comparator<String>() {
            @Override
            public int compare(String s, String s2) {
                if (result1.get(s) > result1.get(s2))
                    return -1;
                return  1;
            }
        });
        ordered.putAll(result);

        int idx = 0;
        for (Iterator<String> iterator = ordered.navigableKeySet().iterator(); iterator.hasNext();) {
            String id = iterator.next();
            if (id.equals(questionId)) {
                break;
            }
            idx += 1;
        }
        return 1.0 / (1.0*(ordered.size()-idx));
    }


    /**
     * Ranks are supposed to already be inversed!
     * @param ranks
     * @return
     */
    public static double calculateMRR(List<Double> ranks){
        Double sum = 0.;
        for (Double rank : ranks) {
            sum += rank;
        }
        return sum / (1.0 * ranks.size());
    }

    public static Double evaluate(List<Result> results) {
        Double retVal = 0.;
        for (Result result : results) {
            retVal += 1.0 / (result.getRankedIds().indexOf(result.getBestId()) + 1.0);
        }
        retVal /= (results.size() * 1.);
        return retVal;
    }

    public static void main(String[] args) {
        List<Result> results = Arrays.asList(new Result[]{
                new Result(Arrays.asList(new String[]{"catten", "cati", "cats"}), "cats"),
                new Result(Arrays.asList(new String[]{"torii", "tori", "toruses"}), "tori"),
                new Result(Arrays.asList(new String[]{"viruses", "virii", "viri"}), "viruses")
        });

        System.out.println("This value: " + MRREvaluator.evaluate(results) + " should equal about 0.61");


        ConcurrentHashMap<String, Double> map = new ConcurrentHashMap<String, Double>();
        map.put("catten", 0.4);
        map.put("cati", 0.7);
        map.put("cats", 0.9);
        System.out.println(calculateMRRRank("cats", map));


    }
}

class Result {
    private List<String> rankedIds;
    private String bestId;

    public Result(List<String> rankedIds, String bestId) {
        this.rankedIds = rankedIds;
        this.bestId = bestId;
    }

    public List<String> getRankedIds() {
        return rankedIds;
    }

    public void setRankedIds(ArrayList<String> rankedIds) {
        this.rankedIds = rankedIds;
    }

    public String getBestId() {
        return bestId;
    }

    public void setBestId(String bestId) {
        this.bestId = bestId;
    }
}
