package edu.mcw.rgd.dao.impl;


import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.MessageCenterMessage;
import edu.mcw.rgd.datamodel.WatchedObject;
import edu.mcw.rgd.datamodel.WatchedTerm;
import edu.mcw.rgd.datamodel.myrgd.MyList;
import edu.mcw.rgd.datamodel.myrgd.MyUser;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.object.SqlUpdate;

import java.sql.Types;
import java.util.Date;
import java.util.List;

/**
 * @author jdepons
 * @since May 19, 2008
 */
public class MyDAO extends AbstractDAO {


    public void insertOrUpdateObjectWatcher(String userId, int rgdId, boolean watchNomen, boolean watchGo,
                               boolean watchDisease, boolean watchPhenotype, boolean watchPathway, boolean watchStrain,boolean watchReference,
                               boolean watchAlteredStrains, boolean watchProtein, boolean watchInteraction, boolean watchRefSeq,boolean watchExdb ) throws Exception {

        System.out.println("entered insert or update");

        List<WatchedObject> wo = this.getWatchedObjects(userId,rgdId);

        if (wo.size() > 0) {
            this.updateObjectWatcher(userId,rgdId,watchNomen,watchGo,watchDisease,watchPhenotype,watchPathway,watchStrain, watchReference,watchAlteredStrains,watchProtein,watchInteraction,watchRefSeq, watchExdb);
        }else {
            this.insertObjectWatcher(userId,rgdId,watchNomen,watchGo,watchDisease,watchPhenotype,watchPathway,watchStrain,watchReference,watchAlteredStrains,watchProtein,watchInteraction,watchRefSeq,watchExdb);
        }

    }


    public int insertObjectWatcher(String userId, int rgdId, boolean watchNomen, boolean watchGo,
                               boolean watchDisease, boolean watchPhenotype, boolean watchPathway, boolean watchStrain, boolean watchReference,
                               boolean watchAlteredStrains, boolean watchProtein, boolean watchInteraction, boolean watchRefSeq, boolean watchExdb ) throws Exception {

        int nomen = watchNomen ? 1 : 0;
        int go = watchGo ? 1 : 0;
        int disease = watchDisease ? 1 : 0;

        int phenotype = 0;
        if (watchPhenotype) {
            phenotype = 1;
        }
        int pathway = 0;
        if (watchPathway) {
            pathway = 1;
        }
        int strain = 0;
        if (watchStrain) {
            strain = 1;
        }
        int reference = 0;
        if (watchReference) {
            reference = 1;
        }
        int alteredStrains = 0;
        if (watchAlteredStrains) {
            alteredStrains = 1;
        }
        int protein = 0;
        if (watchProtein) {
            protein = 1;
        }
        int interaction = 0;
        if (watchInteraction) {
            interaction = 1;
        }
        int refSeq = 0;
        if (watchRefSeq) {
            refSeq = 1;
        }
        int exdb = 0;
        if (watchExdb) {
            exdb = 1;
        }

        String query = "insert into my_rgd_object_watch (username, rgd_id, nomen, go,disease,phenotype,pathway,strain,reference," +
                "altered_strains,protein,interaction,refseq_status,exdb) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        return update(query, userId, rgdId,nomen,go,disease,phenotype,pathway,strain,reference,alteredStrains,protein,interaction,refSeq,exdb);

    }

    public int updateObjectWatcher(String userId, int rgdId, boolean watchNomen, boolean watchGo,
                                   boolean watchDisease, boolean watchPhenotype, boolean watchPathway, boolean watchStrain, boolean watchReference,
                                   boolean watchAlteredStrains, boolean watchProtein, boolean watchInteraction, boolean watchRefSeq, boolean watchExdb ) throws Exception {

        try {

            int nomen = watchNomen ? 1 : 0;
            int go = watchGo ? 1 : 0;
            int disease = watchDisease ? 1 : 0;

            int phenotype = 0;
            if (watchPhenotype) {
                phenotype = 1;
            }
            int pathway = 0;
            if (watchPathway) {
                pathway = 1;
            }
            int strain = 0;
            if (watchStrain) {
                strain = 1;
            }
            int reference = 0;
            if (watchReference) {
                reference = 1;
            }
            int alteredStrains = 0;
            if (watchAlteredStrains) {
                alteredStrains = 1;
            }
            int protein = 0;
            if (watchProtein) {
                protein = 1;
            }
            int interaction = 0;
            if (watchInteraction) {
                interaction = 1;
            }
            int refSeq = 0;
            if (watchRefSeq) {
                refSeq = 1;
            }
            int exdb = 0;
            if (watchExdb) {
                exdb = 1;
            }

            String query = "update my_rgd_object_watch set nomen=? , go=? , disease=? , phenotype=? , pathway=? , strain=?, reference=? , " +
                    "altered_strains=? , protein=? , interaction=? , refseq_status=?, exdb=? where username=? and rgd_id=? ";


            System.out.println("running update");

            int value = update(query, nomen, go, disease, phenotype, pathway, strain, reference, alteredStrains, protein, interaction, refSeq, exdb, userId, rgdId);
            return value;

        }catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }


    public void insertOrUpdateTermWatcher(String userId, String accId, boolean watchRatGene, boolean watchMouseGene,boolean watchHumanGene,boolean watchRatQTL, boolean watchMouseQTL,boolean watchHumanQTL, boolean watchStrain, boolean watchRatVariant) throws Exception {

        List<WatchedTerm> wo = this.getWatchedTerms(userId,accId);

        if (wo.size() > 0) {
            this.updateTermWatcher(userId,accId,watchRatGene, watchMouseGene, watchHumanGene, watchRatQTL, watchMouseQTL, watchHumanQTL, watchStrain,watchRatVariant);
        }else {
            this.insertTermWatcher(userId,accId,watchRatGene, watchMouseGene, watchHumanGene, watchRatQTL, watchMouseQTL, watchHumanQTL, watchStrain,watchRatVariant);
        }

    }


    public int insertTermWatcher(String userId, String accId, boolean watchRatGene, boolean watchMouseGene, boolean watchHumanGene, boolean watchRatQTL, boolean watchMouseQTL, boolean watchHumanQTL, boolean watchStrain, boolean watchRatVariant) throws Exception {


        int ratGene = 0;
        if (watchRatGene) {
            ratGene = 1;
        }
        int mouseGene = 0;
        if (watchMouseGene) {
            mouseGene = 1;
        }
        int humanGene = 0;
        if (watchHumanGene) {
            humanGene = 1;
        }

        int ratQtl = 0;
        if (watchRatQTL) {
            ratQtl = 1;
        }
        int mouseQtl = 0;
        if (watchMouseQTL) {
            mouseQtl = 1;
        }
        int humanQtl = 0;
        if (watchHumanQTL) {
            humanQtl = 1;
        }
        int strain = 0;
        if (watchStrain) {
            strain = 1;
        }
        int ratVariant = 0;
        if (watchRatVariant) {
            ratVariant = 1;
        }

        String query = "insert into my_rgd_ont_watch (username, acc_id, genes_rat, genes_mouse, genes_human, qtls_rat, qtls_mouse, qtls_human, strains, variants_rat) " +
                "values (?,?,?,?,?,?,?,?,?,?)";

        return update(query, userId, accId, ratGene, mouseGene, humanGene, ratQtl, mouseQtl, humanQtl, strain, ratVariant);
    }

    public int updateTermWatcher(String userId, String accId, boolean watchRatGene, boolean watchMouseGene, boolean watchHumanGene, boolean watchRatQTL, boolean watchMouseQTL, boolean watchHumanQTL, boolean watchStrain, boolean watchRatVariant) throws Exception {

        try {

            int ratGene = 0;
            if (watchRatGene) {
                ratGene = 1;
            }
            int mouseGene = 0;
            if (watchMouseGene) {
                mouseGene = 1;
            }
            int humanGene = 0;
            if (watchHumanGene) {
                humanGene = 1;
            }

            int ratQtl = 0;
            if (watchRatQTL) {
                ratQtl = 1;
            }
            int mouseQtl = 0;
            if (watchMouseQTL) {
                mouseQtl = 1;
            }
            int humanQtl = 0;
            if (watchHumanQTL) {
                humanQtl = 1;
            }
            int strain = 0;
            if (watchStrain) {
                strain = 1;
            }
            int ratVariant = 0;
            if (watchRatVariant) {
                ratVariant = 1;
            }

            String query = "update my_rgd_ont_watch set genes_rat=? , genes_mouse=?, genes_human=?, qtls_rat=? , qtls_mouse=?, qtls_human=?, strains=? , variants_rat=? where username=? and acc_id=? ";

            int value = update(query, ratGene, mouseGene, humanGene, ratQtl, mouseQtl, humanQtl, strain, ratVariant, userId, accId);
            return value;

        }catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }



    public int removeTermWatcher(String userId, String accId) throws Exception {

        String query = "delete from my_rgd_ont_watch where username=? and acc_id=?";
        return update(query, userId, accId);
    }


    public int removeObjectWatcher(String userId, int rgdId) throws Exception {

        String query = "delete from my_rgd_object_watch where username=? and rgd_id=?";
        return update(query, userId, rgdId);
    }

    public List<WatchedObject> getWatchedObjects(int rgdId) throws Exception {

        String query = "select my.*, g.gene_symbol from my_rgd_object_watch my, genes g where my.rgd_id=g.rgd_id and my.rgd_id=" + rgdId;

        System.out.println("query = " + query);
        WatchedObjectQuery q = new WatchedObjectQuery(this.getDataSource(),query);
        return execute(q);
    }

    public List<WatchedObject> getWatchedObjects(String username, int rgdId) throws Exception {

        String query = "select my.*, g.gene_symbol from my_rgd_object_watch my, genes g where my.rgd_id=g.rgd_id and my.rgd_id=? and my.username=?";

        WatchedObjectQuery q = new WatchedObjectQuery(this.getDataSource(),query);
        return execute(q, rgdId,username);
    }

    public List<WatchedObject> getWatchedObjects(String username) throws Exception {

        String query = "select my.*, g.gene_symbol from my_rgd_object_watch my, genes g where my.rgd_id=g.rgd_id and my.username=?";

        WatchedObjectQuery q = new WatchedObjectQuery(this.getDataSource(),query);
        return execute(q, username);
    }

    public List<WatchedTerm> getWatchedTerms(String username, String accId) throws Exception {

        String query = "select my.*, ot.term from my_rgd_ont_watch my, ont_terms ot where my.acc_id=ot.term_acc and my.username=? and my.acc_id=?";
        WatchedTermQuery q = new WatchedTermQuery(this.getDataSource(),query);
        return execute(q, username, accId);
    }






    public List<WatchedTerm> getWatchedTerms(String username) throws Exception {

        String query = "select my.*, ot.term from my_rgd_ont_watch my, ont_terms ot where my.acc_id=ot.term_acc and my.username=?";
        WatchedTermQuery slq = new WatchedTermQuery(this.getDataSource(),query);
        slq.declareParameter(new SqlParameter(Types.VARCHAR)); // last modified date
        slq.compile();

        return slq.execute(username);

    }

    public List<WatchedTerm> getAllWatchedTerms() throws Exception {

        String query = "select my.*, ot.term from my_rgd_ont_watch my, ont_terms ot where my.acc_id=ot.term_acc";
        WatchedTermQuery slq = new WatchedTermQuery(this.getDataSource(),query);
        slq.compile();

        return slq.execute();

    }

    public List<String> getAllWatchedUsers() throws Exception {

        String query = "select distinct username from my_rgd_object_watch union select distinct username from my_rgd_ont_watch\n";
        StringListQuery slq = new StringListQuery(this.getDataSource(),query);
        slq.compile();

        return slq.execute();

    }



    public List<WatchedObject> getAllWatchedObjects() throws Exception {

        String query = "select * from my_rgd_object_watch";
        WatchedObjectQuery slq = new WatchedObjectQuery(this.getDataSource(),query);
        slq.compile();

        return slq.execute();

    }

    public boolean myUserExists(String username) throws Exception{
        String query = "select * from my_users where username = '" + username + "'";

        MyUserQuery slq = new MyUserQuery(this.getDataSource(), query);
        slq.compile();
        List l = slq.execute();

        return l.size() > 0;
    }

    public MyUser getMyUser(String username) throws Exception{
        String query = "select * from my_users where username = '" + username + "'";

        MyUserQuery slq = new MyUserQuery(this.getDataSource(), query);
        slq.compile();
        List l = slq.execute();

        if (l.size() > 0  ) {
            return (MyUser) l.get(0);
        }else {
            throw new Exception("User not found :" + username);
        }

    }

    public int updateDigest(String userId, boolean digestSetting) throws Exception {

        String query = "UPDATE my_users SET email_digest=? WHERE username=?";

        int digestInt = digestSetting ? 1 : 0;
        return update(query, digestInt, userId);
    }



    public int updatePassword(String userId, String password) throws Exception {

        String query = "update my_users set password=? where username=?";
        return update(query, password, userId);
    }


    public MyUser insertMyUser(String userId, String passcode, boolean enabled) throws Exception {

        String query = "insert into my_users (username, password, enabled, email_digest) values (?,?,?,?)";

        int enabledInt = enabled ? 1 : 0;

        update(query, userId, passcode, enabledInt, 1);

        MyUser mu = new MyUser();
        mu.setUsername(userId);

        return mu;
    }

    public void insertMyUserRole(String userId, String role) throws Exception {

        String query = "INSERT INTO my_user_roles (username, role) VALUES (?,?)";
        update(query, userId, role);
    }




    public List<MyList> getUserObjectLists(String username) throws Exception {
        String query = "select * from my_rgd_object_list where username = '" + username + "'";

        MyListQuery slq = new MyListQuery(this.getDataSource(), query);
        slq.compile();
        return slq.execute();

    }

    public void deleteUserObjectList(int listId) throws Exception {
        String query = "delete from my_rgd_object_list where list_id = " + listId;

        MyListQuery slq = new MyListQuery(this.getDataSource(), query);
        slq.compile();
        slq.execute();

    }


    public MyList getUserObjectList(int listId) throws Exception {
        String query = "select * from my_rgd_object_list where list_id = " + listId;

        MyListQuery slq = new MyListQuery(this.getDataSource(), query);
        slq.compile();
        slq.execute();

        List<MyList> ret = slq.execute();

        if (ret.size() != 1) {
            throw new Exception("List not found for id " + listId);
        }

        return ret.get(0);

    }

    public List<Gene> getGenes(int listId) throws Exception {
        String query = "SELECT g.* FROM genes g, my_rgd_object_list_item li WHERE li.list_id=? AND li.rgd_id=g.rgd_id";
        return GeneQuery.execute(this, query, listId);
    }

    public int insertList(MyList list) throws Exception{
        System.out.println("entering insert");

        list.setId(this.getNextKey("myList_seq"));

        String query = "insert into my_rgd_object_list (username, list_id, list_name, obj_type, description,link) values (?, ?, ?, ?, ?, ?)";
        System.out.println("running " + query);

        SqlUpdate su = new SqlUpdate(this.getDataSource(), query);

        su.declareParameter(new SqlParameter(Types.VARCHAR)); // last modified date
        su.declareParameter(new SqlParameter(Types.INTEGER)); // last modified date
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // study_type
        su.declareParameter(new SqlParameter(Types.INTEGER)); // study_url
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // study_id
        su.declareParameter(new SqlParameter(Types.CLOB)); // study_id

        su.compile();
        su.update(list.getUsername(), list.getId(), list.getName(), list.getObjectType(), list.getDesc(),list.getLink());

        System.out.println("exiting insert");
        return list.getId();
    }

    public int insertGenes(int listId, List<Integer> geneRgdIds) throws Exception {

        String query = "insert into my_rgd_object_list_item (list_id, rgd_id) values (?,?)";

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(), query);

        su.declareParameter(new SqlParameter(Types.INTEGER)); // last modified date
        su.declareParameter(new SqlParameter(Types.INTEGER)); // last modified date

        for( Integer g: geneRgdIds ) {
            su.update(new Object[]{listId, g});
        }

        return executeBatch(su);
    }

    public int insertMessageCenter(String username, String title, String message) throws Exception {

        String query = "insert into my_rgd_message_center (message_id, username, title, created_date, message) values (?,?,?,?,?)";

        SqlUpdate su = new SqlUpdate(this.getDataSource(), query);

        su.declareParameter(new SqlParameter(Types.INTEGER)); // last modified date
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // last modified date
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // last modified date
        su.declareParameter(new SqlParameter(Types.TIMESTAMP)); // study_type
        su.declareParameter(new SqlParameter(Types.CLOB)); // study_url

        su.compile();

        return su.update(this.getNextKeyFromSequence("MY_MESSAGE_CENTER_SEQ"),username, title, new Date(), message);

    }


    public List<MessageCenterMessage> getAllMessagesFromMessageCenter(String username) throws Exception {
        String query = "select * from my_rgd_message_center where username = ? order by created_date desc";

        MyMessageCenterQuery slq = new MyMessageCenterQuery(this.getDataSource(), query);
        slq.declareParameter(new SqlParameter(Types.VARCHAR)); // last modified date

        slq.compile();

        return slq.execute(username);

    }

    public List<MessageCenterMessage> getAllMessagesFromMessageCenterWithoutPayload(String username) throws Exception {
        String query = "SELECT * FROM my_rgd_message_center WHERE username=? ORDER BY created_date DESC";

        MyMessageCenterLiteQuery q = new MyMessageCenterLiteQuery(this.getDataSource(), query);
        return execute(q, username);
    }


    public void deleteMessageCenterMessage(int messageId, String username) throws Exception {
        String query = "DELETE FROM my_rgd_message_center WHERE message_id=? AND username=?";
        update(query, messageId, username);
    }

    public List<MessageCenterMessage> getMessagesFromMessageCenter(int messageId, String username) throws Exception {
        String query = "SELECT * FROM my_rgd_message_center WHERE message_id=? AND username=?";

        System.out.println("username = " + username);

        MyMessageCenterQuery q = new MyMessageCenterQuery(this.getDataSource(), query);
        return execute(q, messageId, username);
    }


}