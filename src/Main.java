import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Main {

    public static void main(String[] args) {

        //需要解析的索引文件
        String sampleFilePath = args[0];
//        String sampleFilePath = "/Users/laphael/Desktop/ncbi/sample9_err.out";

//        String sample_accession = args[1];
        File file = new File(sampleFilePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            NodeList sample = doc.getElementsByTagName("BioSample");
            int createCount = 0;
            int releaseCount = 0;
            int accessionCount = 0;
            int sraCount = 0;
            int orgCount = 0;
            for (int i = 0; i < sample.getLength(); i++) {
//                String newGsaAccession = addSampleAcc(sample_accession);
//                sample_accession = newGsaAccession;
                Element sampleE = (Element) sample.item(i);
                String createTime = sampleE.getAttribute("submission_date");
                int create_flag = 1;
                if(createTime!=null&&createTime!=""){
                    createTime =createTime.substring(0,10);
                } else {
                    createCount++;
                    createTime = "2015-01-01";
                    create_flag = 0;
                }
                int release_flag = 1;
                String releaseTime = sampleE.getAttribute("publication_date");
                if(releaseTime!=null&&releaseTime!=""){
                    releaseTime =releaseTime.substring(0,10);
                } else {
                    releaseCount++;
                    //releaseTime = "2015-01-01";
                    release_flag = 0;
                }
                String accession = sampleE.getAttribute("accession");
//                if(accession==null||accession==""){
//                    accessionCount++;
//                }

                //name and sra
                NodeList idList= sampleE.getElementsByTagName("Id");
                Element idsNode =  (Element) idList.item(0);
                String name = "";
//                String sra = "";
//                if(idsNode!=null){
//                    for(int id=0;id<idList.getLength();id++){
//                        Element idNode =  (Element) idList.item(id);
//                        String db =idNode.getAttribute("db");
//                        String sampleName =idNode.getAttribute("db_label");
//                        if(sampleName.equals("Sample name")){
//                            name =idNode.getFirstChild().getNodeValue();
//                        }
//                        if(db.equals("SRA")){
//                            String strSra =idNode.getFirstChild().getNodeValue();
//                            if(sra!=""&&sra!=null){
//                                sra = sra+"&&&"+strSra;
//                            } else {
//                                sra = strSra;
//                            }
//
//                        }
//                    }
//                }
//                if(sra.contains("&&&")){
//                    sraCount++;
//                }
//                System.out.println("name="+name+"|sra="+sra);
                NodeList descList= sampleE.getElementsByTagName("Description");
                Element descNode =  (Element) descList.item(0);
                //title
                NodeList titleList= descNode.getElementsByTagName("Title");
                Element titleNode =  (Element) titleList.item(0);
                String title =titleNode.getFirstChild().getNodeValue();
//                System.out.println(title);
                //物种ID
                NodeList organismList= descNode.getElementsByTagName("Organism");
                Element organismNode =  (Element) organismList.item(0);
                String taxonomy_id =organismNode.getAttribute("taxonomy_id");
                String taxonomy_name =organismNode.getAttribute("taxonomy_name");
//                System.out.println(taxonomy_id);
                //描述
                NodeList paragraphList= descNode.getElementsByTagName("Paragraph");
                Element paraNode =  (Element) paragraphList.item(0);
                String desc = "";
                String key = "";
//                int keyCounts=0;
                if(paraNode!=null){
                    for(int pa=0;pa<paragraphList.getLength();pa++){
                        Element paNode =  (Element) paragraphList.item(pa);
                        if(paNode.getFirstChild()==null){
                            continue;
                        } else {
                            if(paNode.getFirstChild().getNodeValue().contains("Keywords")) {
//                          keyCounts++;
                                key = paNode.getFirstChild().getNodeValue();
                            } else {
                                desc =desc + paNode.getFirstChild().getNodeValue();
                            }
                        }
                    }
                }
                //Package
                String packageString = "";
                NodeList packageList= sampleE.getElementsByTagName("Package");
                Element packageNode =  (Element) packageList.item(0);
                if(packageNode!=null){
                    packageString =packageNode.getAttribute("display_name");
                }
                //Model
                String model = "";
                NodeList modelList= sampleE.getElementsByTagName("Model");
                Element modelNode =  (Element) modelList.item(0);
                if(modelNode!=null){
                    if(modelNode.getFirstChild()!=null){
                        model =modelNode.getFirstChild().getNodeValue();
                    }
                }
                //Owner
                String owner_name = "";
                String owner_abbreviation = "";
                String contact_email = "";
                NodeList ownerList= sampleE.getElementsByTagName("Owner");
                Element ownerNode =  (Element) ownerList.item(0);
                if(ownerNode!=null){
                    NodeList ownerNameList= ownerNode.getElementsByTagName("Name");
                    Element ownerNameNode =  (Element) ownerNameList.item(0);
                    if(ownerNameNode!=null){
                        owner_abbreviation =ownerNameNode.getAttribute("abbreviation");
                        if(ownerNameNode.getFirstChild()!=null){
                            owner_name =ownerNameNode.getFirstChild().getNodeValue();
                        }
                    }
                    NodeList contactList= ownerNode.getElementsByTagName("Contact");
                    Element contactNode =  (Element) contactList.item(0);
                    if(contactNode!=null){
                        contact_email = contactNode.getAttribute("email");
                    }
                }

               // System.out.println(descList.getLength()+","+keyCounts);
                //Attribute
                NodeList attrList= sampleE.getElementsByTagName("Attribute");
                Element attrNode =  (Element) attrList.item(0);
                String attribute = "{";
                if(attrNode!=null){
                    for(int attr=0;attr<attrList.getLength();attr++){
                        Element aNode =  (Element) attrList.item(attr);
                        String attrName = aNode.getAttribute("attribute_name");
                        String value = "";
                        if(aNode.getFirstChild()!=null){
                            value=aNode.getFirstChild().getNodeValue();
                        }
//                        if(attrName.contains("'s")){
//                            attrName = attrName.replace("'s","\\'s");
//                        }
                        if(attrName.contains("\n")){
                            attrName = attrName.replace("\n","");
                        }
                        if(attrName.contains("\\n")){
                            attrName = attrName.replace("\\n","\\\\n");
                        }
                        if(attrName.contains("\t")){
                            attrName = attrName.replace("\t","");
                        }
                        if(attrName.contains("#")){
                            attrName = attrName.replace("#","\\#");
                        }
                        if(attrName.contains("&quot")){
                            attrName = attrName.replace("&quot","");
                        }
                        if(attrName.contains("\"")){
                            attrName = attrName.replace("\"","");
                            if(attrName.contains("'")){
                                attrName = attrName.replace("'","\\'");
                            }
                            attrName =  "\""+attrName+"\"";
                        } else {
                            if(attrName.contains("'")){
                                attrName = attrName.replace("'","\\'");
                            }
                            attrName = "\""+attrName+"\"";
                        }

                        if(value.contains("ð")&&value.contains("½")){
                            String begin = value.split("ð")[0];
                            String len = value.split("ð")[1];
                            String end = len.split("½")[1];
                            String un = len.split("½")[0];
                            if(checkRegexp(un,1)){
                                value = begin + " " + end;
                            }

                        }
                        if(value.contains("\n")){
                            value = value.replace("\n","");
                        }
                        if(value.contains("ð½")){
                            value = value.replace("ð½"," ");
                        }
                        if(value.contains("\\n")&&!value.contains("\\\\n")){
                            value = value.replace("\\n","\\\\n");
                        }
                        if(value.contains("\t")){
                            value = value.replace("\t","");
                        }
                        if(value.contains("#")){
                            value = value.replace("#","\\#");
                        }
                        if(value.contains("\\")){
                            value = checkString(value);
                        }
                        if(value.contains("&quot")){
                            value = value.replace("&quot","");
                        }
                        if(value.contains("\"")){
                            value = value.replace("\"","");
                            if(value.contains("'")){
                                value = value.replace("'","\\'");
//                                value = "'"+value+"'";
                            }
                            value = "\""+value+"\"";
                        } else {
                            if(value.contains("'")){
                                value = value.replace("'","\\'");
                            }
                            value = "\""+value+"\"";
                        }
                        if(attr==0) {
                            attribute = attribute + attrName +":"+value;
                        } else {
                            attribute = attribute + "," + attrName +":"+value;
                        }

                    }
                }
                attribute =attribute +"}";
//                System.out.println(attribute);
                NodeList orgList= sampleE.getElementsByTagName("Name");
                Element  orge =  (Element) orgList.item(0);
                String organization = "";
//                System.out.println(accession);
                if(orge!=null){
                    for(int org=0;org<orgList.getLength();org++){
                        Element orgeNode =  (Element) orgList.item(org);
                        if(orgeNode.getFirstChild()!=null){
                            String orgName = orgeNode.getFirstChild().getNodeValue();
                            if(!orgName.contains("First")&&!orgName.contains("Last")&&(!orgName.contains("<")&&!orgName.contains(">"))&&!orgName.contains("\n" + "          ")){
                                if(org==0){
                                    organization = orgName;
                                } else {
                                    organization = organization + "&&&"+ orgName;
                                }
                            }
                        } else {
                            organization = "NCBI";
                        }
                    }
                } else {
                    organization = "NCBI";
                }
                if(organization.endsWith("\\")){
                    organization = organization.substring(0,organization.length()-1);
                }
//                if(organization.contains("&&&")){
//                    orgCount++;
//                }
                NodeList conList= sampleE.getElementsByTagName("Contact");
                Element  con =  (Element) conList.item(0);
                String email = "";
                if(con!=null){
                    email = con.getAttribute("email");
                }
                if(email.equals("")){
                    email="gsa-submit@big.ac.cn";
                }
                //System.out.println(email);
//                System.out.println(accession+":"+organization);
                //submitter
                String insertSubmitterSql = "insert into sample_submitter(first_name,last_name,email,organization,department,street,city,postal_code,country_id,prj_accession)";
                insertSubmitterSql = insertSubmitterSql + " values('GSA','GSA','"+email+"',";
                organization = organization.replace("'s","\\'s");
                if(organization.contains("\"")){
                    if(organization.contains("'")&&!organization.contains("'s")){
                        organization = organization.replace("'","\\'");
                        insertSubmitterSql = insertSubmitterSql +  "'"+organization+"','National Genomics Data Center','No.1 Beichen West Road, Chaoyang District','Beijing','100101',45,'"+accession+"');";
                    } else {
                        insertSubmitterSql = insertSubmitterSql +  "'"+organization+"','National Genomics Data Center','No.1 Beichen West Road, Chaoyang District','Beijing','100101',45,'"+accession+"');";
                    }
                } else {
                    insertSubmitterSql = insertSubmitterSql +  "\""+organization+"\",'National Genomics Data Center','No.1 Beichen West Road, Chaoyang District','Beijing','100101',45,'"+accession+"');";
                }
                //System.out.println(insertSubmitterSql);
                String insertSampleSql = "";
                if(attribute.equals("{}")){
                    insertSampleSql = "insert into sample(second_accession,user_id,name,taxon_id,taxon_name,title,public_description,release_time,create_time,status,release_state,submission_status,release_flag,create_flag,keyword,package,model,owner_name,owner_abbreviation,contact_email) " +
                            " values('"+accession+"',2930,";
                } else {
                    insertSampleSql = "insert into sample(second_accession,attributes,user_id,name,taxon_id,taxon_name,title,public_description,release_time,create_time,status,release_state,submission_status,release_flag,create_flag,keyword,package,model,owner_name,owner_abbreviation,contact_email) " +
                            " values('"+accession+"','"+attribute+"',2930,";
                }
                if(name.contains("\"")){
                    if(name.contains("'")){
                        name = name.replace("'","\\'");
                        insertSampleSql = insertSampleSql +"'"+name+"',"+taxonomy_id+",";
                    } else {
                        insertSampleSql = insertSampleSql +"'"+name+"',"+taxonomy_id+",";
                    }
                } else {
                    insertSampleSql = insertSampleSql +"\""+name+"\","+taxonomy_id+",";
                }

                if(taxonomy_name.contains("\"")){
                    if(taxonomy_name.contains("'")){
                        taxonomy_name = taxonomy_name.replace("'","\\'");
                        insertSampleSql = insertSampleSql +"'"+taxonomy_name+"',";
                    } else {
                        insertSampleSql = insertSampleSql +"'"+taxonomy_name+"',";
                    }
                } else {
                    insertSampleSql = insertSampleSql +"\""+taxonomy_name+"\",";
                }
                if(title.contains("ð")&&title.contains("½")){
                    String begin = title.split("ð")[0];
                    String len = title.split("ð")[1];
                    String end = len.split("½")[1];
                    String un = len.split("½")[0];
                    if(checkRegexp(un,1)){
                        title = begin + " " + end;
                    }
                }
                if(title.contains("ð½")){
                    title = title.replace("ð½"," ");
                }
                if(title.contains("\"")){
                    if(title.contains("'")){
                        title = title.replace("'","\\'");
                        insertSampleSql = insertSampleSql +"'"+title+"',";
                    } else {
                        insertSampleSql = insertSampleSql +"'"+title+"',";
                    }
                } else {
                    insertSampleSql = insertSampleSql +"\""+title+"\",";
                }

                if(desc.endsWith("\\")){
                    desc = desc.replace("\\","\\\\");
                }
                if(desc.contains("\"")){
                    if(desc.contains("'")){
                        desc = desc.replace("'","\\'");
                        insertSampleSql = insertSampleSql +"'"+desc+"','"+releaseTime+"','"+createTime+"',3,2,3,'"+release_flag+"','"+create_flag+"',";
                    } else {
                        insertSampleSql = insertSampleSql +"'"+desc+"','"+releaseTime+"','"+createTime+"',3,2,3,'"+release_flag+"','"+create_flag+"',";
                    }
                } else {
                    insertSampleSql = insertSampleSql +"\""+desc+"\",'"+releaseTime+"','"+createTime+"',3,2,3,'"+release_flag+"','"+create_flag+"',";
                }
                if(key.endsWith("\\")){
                    key = key.substring(0,key.length()-1);
                }
                if(key.contains("\"")){
                    if(key.contains("'")){
                        key = key.replace("'","\\'");
                        insertSampleSql = insertSampleSql +"'"+key+"',";
                    } else {
                        insertSampleSql = insertSampleSql +"'"+key+"',";
                    }
                } else {
                    insertSampleSql = insertSampleSql +"\""+key+"\",";
                }
                if(packageString.endsWith("\\")){
                    packageString = packageString.substring(0,packageString.length()-1);
                }
                if(packageString.contains("\"")){
                    if(packageString.contains("'")){
                        packageString = packageString.replace("'","\\'");
                        insertSampleSql = insertSampleSql +"'"+packageString+"',";
                    } else {
                        insertSampleSql = insertSampleSql +"'"+packageString+"',";
                    }
                } else {
                    insertSampleSql = insertSampleSql +"\""+packageString+"\",";
                }
                if(model.endsWith("\\")){
                    model = model.substring(0,model.length()-1);
                }
                if(model.contains("\"")){
                    if(model.contains("'")){
                        model = model.replace("'","\\'");
                        insertSampleSql = insertSampleSql +"'"+model+"',";
                    } else {
                        insertSampleSql = insertSampleSql +"'"+model+"',";
                    }
                } else {
                    insertSampleSql = insertSampleSql +"\""+model+"\",";
                }
                if(owner_name.endsWith("\\")){
                    owner_name = owner_name.substring(0,owner_name.length()-1);
                }
                if(owner_name.contains("\"")){
                    if(owner_name.contains("'")){
                        owner_name = owner_name.replace("'","\\'");
                        insertSampleSql = insertSampleSql +"'"+owner_name+"',";
                    } else {
                        insertSampleSql = insertSampleSql +"'"+owner_name+"',";
                    }
                } else {
                    insertSampleSql = insertSampleSql +"\""+owner_name+"\",";
                }
                if(owner_abbreviation.endsWith("\\")){
                    owner_abbreviation = owner_abbreviation.substring(0,owner_abbreviation.length()-1);
                }
                if(owner_abbreviation.contains("\"")){
                    if(owner_abbreviation.contains("'")){
                        owner_abbreviation = owner_abbreviation.replace("'","\\'");
                        insertSampleSql = insertSampleSql +"'"+owner_abbreviation+"',";
                    } else {
                        insertSampleSql = insertSampleSql +"'"+owner_abbreviation+"',";
                    }
                } else {
                    insertSampleSql = insertSampleSql +"\""+owner_abbreviation+"\",";
                }
                if(contact_email.endsWith("\\")){
                    contact_email = contact_email.substring(0,contact_email.length()-1);
                }
                if(contact_email.contains("\"")){
                    if(contact_email.contains("'")){
                        contact_email = contact_email.replace("'","\\'");
                        insertSampleSql = insertSampleSql +"'"+contact_email+"');";
                    } else {
                        insertSampleSql = insertSampleSql +"'"+contact_email+"');";
                    }
                } else {
                    insertSampleSql = insertSampleSql +"\""+contact_email+"\");";
                }
                System.out.println(insertSampleSql);

            }
//            System.out.println(orgCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String addSampleAcc(String sample_accession) {
        String prefix = sample_accession.substring(0, 4);
//        char alphaCode = sample_accession.charAt(4);
        String numCode = sample_accession.substring(4);
        int num = Integer.parseInt(numCode) + 1;
        String newAccession = prefix  + num;
        return newAccession;
    }
    public static String checkString(String value) {
        String re = "";
       if(value.contains("\\b")){
            value = value.replace("\\b","\\\\b");
        } else if(value.contains("\\r")&&!value.contains("\\\\r")){
            value = value.replace("\\r","\\\\r");
        }else if(value.contains("\\%")){
           value = value.replace("\\%","%");
       }else if(value.contains("\\0")){
           value = value.replace("\\0","0");
       } else if(value.contains("\\\\")){
            value = value.replace("\\\\","\\\\\\\\");
       }
        re = value;
        return re;
    }

    public static boolean checkRegexp(String name, int flag){
        boolean checkFlag = true;
        Boolean strResult = true;
        if(flag==1) {
            strResult = name.matches("[^\\u0000-\\u007F]*$");
        }
        if(!strResult){
            checkFlag = false;
        }
        return  checkFlag;
    }
}
