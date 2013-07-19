package si.zitnik.research.interestmining.util;

import java.sql.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 7/4/13
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class EvidenceToHashMapMapper {
    //MSSQL Example
    //private final String connectionUrl = "jdbc:sqlserver://192.168.7.65\\SQLEXPRESS:1433;databaseName=Concept_Extraction;user=sa;password=xs;";
    //private final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=yahooL6;integratedSecurity=true";
    private final String connectionUrl = "jdbc:mysql://localhost:3306/interestminingL61k?user=slavkoz&password=xs";
    private String informationSourceId = null;

    public EvidenceToHashMapMapper(String informationSourceId) {
        this.informationSourceId = informationSourceId;
    }

    /**
     * Returns all evidences from questions.
     *
     * @return
     * @throws Exception
     */
    public HashMap<String, HashMap<String, Double>> generateQuestionHashMaps() throws Exception {
        HashMap<String, HashMap<String, Double>> retVal = new HashMap<String, HashMap<String, Double>>();

        Connection connection = DriverManager.getConnection(connectionUrl);
        for (String postId : getAllQuestionIds(connection)) {
            HashMap<String, Double> postMap = new HashMap<String, Double>();
            ResultSet rs = getEvidences(connection, postId);
            while (rs.next()) {
                String keyword = rs.getString("keyword");
                Double weight = rs.getDouble("weight");
                postMap.put(keyword, weight);
            }
            retVal.put(postId, postMap);
        }
        connection.close();

        return retVal;
    }

    /**
     * Returns all evidences from answers.
     *
     * @return
     * @throws Exception
     */
    public HashMap<String, HashMap<String, Double>> generateAnswersHashMaps() throws Exception {
        HashMap<String, HashMap<String, Double>> retVal = new HashMap<String, HashMap<String, Double>>();

        Connection connection = DriverManager.getConnection(connectionUrl);
        for (String postId : getAllAnswerIds(connection)) {
            HashMap<String, Double> postMap = new HashMap<String, Double>();
            ResultSet rs = getEvidences(connection, postId);
            while (rs.next()) {
                String keyword = rs.getString("keyword");
                Double weight = rs.getDouble("weight");
                postMap.put(keyword, weight);
            }
            retVal.put(postId, postMap);
        }
        connection.close();

        return retVal;
    }

    /**
     * Return all evidences for users from questions. For specific user, all evidences for his questions are aggregated.
     *
     * Takes Evidences ONLY from questions.
     *
     * @return
     * @throws Exception
     */
    public HashMap<String, HashMap<String, Double>> generateQuestionUserHashMaps() throws Exception {
        HashMap<String, HashMap<String, Double>> retVal = new HashMap<String, HashMap<String, Double>>();

        Connection connection = DriverManager.getConnection(connectionUrl);
        for (String userId : getAllUserIds(connection)) {
            HashMap<String, Double> userMap = new HashMap<String, Double>();
            ResultSet rs = getAllQuestionEvidencesByUserId(connection, userId);
            while (rs.next()) {
                String keyword = rs.getString("keyword");
                Double weight = rs.getDouble("weight");
                if (userMap.containsKey(keyword)) {
                    userMap.put(keyword, probTCoNorm(weight, userMap.get(keyword)));
                } else {
                    userMap.put(keyword, weight);
                }
            }
            retVal.put(userId, userMap);
        }
        connection.close();

        return retVal;
    }

    /**
     * Return all evidences for users from answers. For specific user, all evidences for his answers are aggregated.
     * (In L6 we have only data for best answerers)
     *
     * Takes Evidences ONLY from answers.
     *
     * @return
     * @throws Exception
     */
    public HashMap<String, HashMap<String, Double>> generateAnswerUserHashMaps() throws Exception {
        HashMap<String, HashMap<String, Double>> retVal = new HashMap<String, HashMap<String, Double>>();

        Connection connection = DriverManager.getConnection(connectionUrl);
        for (String userId : getAllUserIds(connection)) {
            HashMap<String, Double> userMap = new HashMap<String, Double>();
            ResultSet rs = getAllAnswerEvidencesByUserId(connection, userId);
            while (rs.next()) {
                String keyword = rs.getString("keyword");
                Double weight = rs.getDouble("weight");
                if (userMap.containsKey(keyword)) {
                    userMap.put(keyword, probTCoNorm(weight, userMap.get(keyword)));
                } else {
                    userMap.put(keyword, weight);
                }
            }
            retVal.put(userId, userMap);
        }
        connection.close();

        return retVal;
    }


    /**
     * Return all evidences for specific user without those, related to questionId.
     *
     * Takes Evidences according to enum.
     *
     * @return
     * @throws Exception
     */
    public HashMap<String, Double> generateUserHashMapForPost(String userId, EvidenceType evidenceType, String questionIdToRemove) throws Exception {
        Connection connection = DriverManager.getConnection(connectionUrl);
        HashMap<String, Double> userMap = new HashMap<String, Double>();

        if(evidenceType == EvidenceType.QUESTION) {
            //Evidences from all questions, that this user asked \ question with questionId
            for (String questionId : getAllQuestionIdsForUser(connection, userId)) {
                if (!questionId.equals(questionIdToRemove)) {
                    HashMap<String, Double> postMap = generatePostHashMap(questionId);
                    for (String keyword : postMap.keySet()) {
                        if (userMap.containsKey(keyword)) {
                            userMap.put(keyword, probTCoNorm(postMap.get(keyword), userMap.get(keyword)));
                        } else {
                            userMap.put(keyword, postMap.get(keyword));
                        }
                    }
                }
            }
        }
        else if(evidenceType == EvidenceType.ANSWER) {
            //Evidences from all answers (best), that this user answered without those related to  questionId
            for (String answerId : getAllAnswerIdsForUser(connection, userId)) {
                if (!getParentId(connection, answerId).equals(questionIdToRemove)) {
                    HashMap<String, Double> postMap = generatePostHashMap(answerId);
                    for (String keyword : postMap.keySet()) {
                        if (userMap.containsKey(keyword)) {
                            userMap.put(keyword, probTCoNorm(postMap.get(keyword), userMap.get(keyword)));
                        } else {
                            userMap.put(keyword, postMap.get(keyword));
                        }
                    }
                }
            }
        }
        else if(evidenceType == EvidenceType.THREAD) {
            //Evidences from all threads(best answer and other + question), that this user answered/asked without those related to questionId
            //combine ids and remove the one
            Set<String> userQuestionIds = new HashSet<String>(getAllQuestionIdsForUser(connection, userId));
            for (String answerId : getAllAnswerIdsForUser(connection, userId)) {
                userQuestionIds.add(getParentId(connection, answerId));
            }
            userQuestionIds.remove(questionIdToRemove);

            //get ids for threads
            HashSet<String> postsIds = new HashSet<String>();
            for (String questionId : userQuestionIds) {
                postsIds.add(questionId);
                postsIds.addAll(getAllAnswerIdsForQuestion(connection, questionId));
            }

            //do final
            for (String postId : postsIds) {
                HashMap<String, Double> postMap = generatePostHashMap(postId);
                for (String keyword : postMap.keySet()) {
                    if (userMap.containsKey(keyword)) {
                        userMap.put(keyword, probTCoNorm(postMap.get(keyword), userMap.get(keyword)));
                    } else {
                        userMap.put(keyword, postMap.get(keyword));
                    }
                }
            }
        }

        connection.close();

        return userMap;
    }


    /**
     * Returns all evidences from a specific post.
     *
     * @return
     * @throws Exception
     */
    public HashMap<String, Double> generatePostHashMap(String postId) throws Exception {

        Connection connection = DriverManager.getConnection(connectionUrl);
        HashMap<String, Double> postMap = new HashMap<String, Double>();
        ResultSet rs = getEvidences(connection, postId);
        while (rs.next()) {
            String keyword = rs.getString("keyword");
            Double weight = rs.getDouble("weight");
            postMap.put(keyword, weight);
        }
        connection.close();

        return postMap;
    }

    public List<String> getAllUserIds() throws SQLException {
        Connection connection = DriverManager.getConnection(connectionUrl);

        return getAllUserIds(connection);
    }
    //DATABASE STUFF
    private List<String> getAllUserIds(Connection con) throws SQLException {
        ArrayList<String> retVal = new ArrayList<String>();

        PreparedStatement stmt = con.prepareStatement("SELECT id FROM Users;");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            retVal.add(rs.getString("id"));
        }

        return retVal;
    }

    private String getParentId(Connection con, String answerId) throws SQLException {

        PreparedStatement stmt = con.prepareStatement("SELECT parentId FROM Posts WHERE id = ?;");
        stmt.setString(1, answerId);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getString("parentId");
    }

    private List<String> getAllQuestionIdsForUser(Connection con, String userId) throws SQLException {
        ArrayList<String> retVal = new ArrayList<String>();

        PreparedStatement stmt = con.prepareStatement("" +
                "SELECT p.id AS id " +
                "FROM " +
                "   Users u, " +
                "   Posts p " +
                "WHERE " +
                "   u.id = ? AND " +
                "   u.id = p.ownerUserId AND" +
                "   p.postTypeId = 1;");
        stmt.setString(1, userId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            retVal.add(rs.getString("id"));
        }

        return retVal;
    }

    private List<String> getAllAnswerIdsForUser(Connection con, String userId) throws SQLException {
        ArrayList<String> retVal = new ArrayList<String>();

        PreparedStatement stmt = con.prepareStatement("" +
                "SELECT p.id AS id " +
                "FROM " +
                "   Users u, " +
                "   Posts p " +
                "WHERE " +
                "   u.id = ? AND " +
                "   u.id = p.ownerUserId AND" +
                "   p.postTypeId = 2;");
        stmt.setString(1, userId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            retVal.add(rs.getString("id"));
        }

        return retVal;
    }

    private List<String> getAllAnswerIdsForQuestion(Connection con, String questionId) throws SQLException {
        ArrayList<String> retVal = new ArrayList<String>();

        PreparedStatement stmt = con.prepareStatement("SELECT id FROM Posts WHERE postTypeId = 2 AND parentId = ?;");
        stmt.setString(1, questionId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            retVal.add(rs.getString("id"));
        }

        return retVal;
    }

    private List<String> getAllAnswerIds(Connection con) throws SQLException {
        ArrayList<String> retVal = new ArrayList<String>();

        PreparedStatement stmt = con.prepareStatement("SELECT id FROM Posts WHERE postTypeId = 2;");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            retVal.add(rs.getString("id"));
        }

        return retVal;
    }

    private List<String> getAllQuestionIds(Connection con) throws SQLException {
        ArrayList<String> retVal = new ArrayList<String>();

        PreparedStatement stmt = con.prepareStatement("SELECT id FROM Posts WHERE postTypeId = 1;");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            retVal.add(rs.getString("id"));
        }

        return retVal;
    }

    private ResultSet getAllQuestionEvidencesByUserId(Connection con, String userId) throws SQLException {
        return getEvidences(con, userId, 1);
    }

    private ResultSet getAllAnswerEvidencesByUserId(Connection con, String userId) throws SQLException {
        return getEvidences(con, userId, 2);
    }

    private ResultSet getEvidences(Connection con, String userId, int postType) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT e.keyword AS keyword, e.weight AS weight " +
                        "FROM " +
                        "   Evidence e, " +
                        "   EvidencePost ep, " +
                        "   Posts p, " +
                        "   Users u " +
                        "WHERE " +
                        "   u.id = ? AND " +
                        "   p.postTypeId = ? AND " +
                        "   e.informationSourceId = ? AND " +
                        "   u.id = p.ownerUserId AND " +
                        "   p.id = ep.idPost AND " +
                        "   ep.idEvidence = e.id " +
                        "ORDER BY e.keyword ASC; ");
        stmt.setString(1, userId);
        stmt.setInt(2, postType);
        stmt.setString(3, informationSourceId);
        return stmt.executeQuery();
    }

    private ResultSet getEvidences(Connection con, String postId) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT e.keyword AS keyword, e.weight AS weight " +
                        "FROM " +
                        "   Evidence e, " +
                        "   EvidencePost ep, " +
                        "   Posts p " +
                        "WHERE " +
                        "   p.id = ? AND " +
                        "   e.informationSourceId = ? AND " +
                        "   p.id = ep.idPost AND " +
                        "   ep.idEvidence = e.id " +
                        "ORDER BY e.keyword ASC; ");
        stmt.setString(1, postId);
        stmt.setString(2, informationSourceId);
        return stmt.executeQuery();
    }

    //UTILS
    public static double probTCoNorm(double a, double b){
        return a+b - a*b;
    }

    //MAIN METHOD
    public static void main(String[] args) throws Exception {
        EvidenceToHashMapMapper mapper = new EvidenceToHashMapMapper("ConceptExtractor");

        /*
        //post evidences
        HashMap<String, HashMap<String, Double>> map1 = mapper.generateQuestionHashMaps();
        HashMap<String, HashMap<String, Double>> map2 = mapper.generateAnswersHashMaps();
        //user evidences
        HashMap<String, HashMap<String, Double>> map3 = mapper.generateQuestionUserHashMaps();
        HashMap<String, HashMap<String, Double>> map4 = mapper.generateAnswerUserHashMaps();
        */

        HashMap<String, Double> map5 = mapper.generatePostHashMap("2421111");
        HashMap<String, Double> map6 = mapper.generateUserHashMapForPost("u1503147", EvidenceType.ANSWER, "2421111");
        HashMap<String, Double> map7 = mapper.generateUserHashMapForPost("u1503147", EvidenceType.QUESTION, "2421111");
        HashMap<String, Double> map8 = mapper.generateUserHashMapForPost("u1503147", EvidenceType.THREAD, "2421111");

        System.out.println("TEST FINISHED");
    }
}
