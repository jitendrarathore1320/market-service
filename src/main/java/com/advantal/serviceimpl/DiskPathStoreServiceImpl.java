package com.advantal.serviceimpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.advantal.service.DiskPathStoreService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DiskPathStoreServiceImpl implements DiskPathStoreService {

    private static Logger LOGGER = LoggerFactory.getLogger(DiskPathStoreServiceImpl.class);

//    @Autowired private DiskPathStoreRepository diskPathStoreRepository;
//
//    @Autowired private MongoTemplate mongoTemplate;
//
//    @Autowired
//    private HttpServletRequest request;
    private static String OS = System.getProperty("os.name").toLowerCase();
//
//    @Override
//    public String saveDiskPath(DiskPathRequest diskPathRequest) {
//        Query query = new Query(Criteria.where("key").is("storagePath"));
//        Update update = new Update();
//        update.set("key","storagePath");
//        update.set("value",diskPathRequest.getValue());
//        update.set("dateTime",new Date());
//        mongoTemplate.upsert(query, update, "disk_path_store");
//        return "Success";
//    }
//    @Override
//    public DiskPathStore getDiskPath(String value) {
//        Optional<DiskPathStore> diskPathStore = this.diskPathStoreRepository.findByValue(value);
//        return diskPathStore.orElse(null);
//    }
//    @Override
//    public Map<String, Object> getDiskInfoUsingPath() {
//        Map<String, Object> map = new HashMap<>();
//        //get the path here from the db
//        String path = "";
//        //getting the path from db using the key
//        List<DiskPathStore> storagePath = this.diskPathStoreRepository.findOneByKey("storagePath");
//        DecimalFormat df = new DecimalFormat("0.00");
//        if (storagePath.isEmpty()){
//            LOGGER.info("<<<<<<<<<< StoragePath >>>> not found in database >>>>>> : "+storagePath);
//            map.put("message","path not found in database");
//            return map;
//        }
//
//        LOGGER.info("<<<<<<<<<< StoragePath >>>> is getting from db is set >>>>>> : "+storagePath);
//        path = storagePath.get(0).getValue();
//        LOGGER.info("<<<<<<<<<< Path >>>> is set >>>>>> : "+path);
//        try
//        {
//            if(isUnix()) {
//                LOGGER.info("<<<<<<<<<< Linux >>>> OS is set >>>>>> : "+isUnix());
//                File root = new File("/");
//                LOGGER.info("<<<<<<<<<< File >>>> drive is set >>>>>> : "+root);
//                long gigabytes = 1073741824;//1GB
//                double totalSpace = (double) root.getTotalSpace() / gigabytes;
//                double freeSpace = (double) root.getFreeSpace() / gigabytes;
//                double usedSpace = totalSpace - freeSpace;
//
//                //calculate percentage here and add into map
//                long freePercentage = (long) ((totalSpace - usedSpace) / totalSpace * 100);
//                double usedPercentage = (totalSpace - freeSpace) / totalSpace * 100;
//                map.put("totalSpace", (long) totalSpace + "GB");
//                map.put("usedSpace", (long) usedSpace + "GB");
//                map.put("freeSpace", (long) freeSpace + "GB");
//                map.put("freeDataPercentage", df.format(freePercentage));
//                map.put("usedDataPercentage", df.format(usedPercentage));
//                return map;
//            }
//            else if(isWindows()) {
//                LOGGER.info("<<<<<<<<<< Windows >>>> OS is set >>>>>> : "+isWindows());
//                File root = new File(path);
//                LOGGER.info("<<<<<<<<<< File >>>> drive is set >>>>>> : "+root);
////                System.out.println(root.exists());
//                if(root.exists()) {
//                    long gigabytes = 1073741824;//1GB
//                    double totalSpace = (double) root.getTotalSpace() / gigabytes;
//                    double freeSpace = (double) root.getFreeSpace() / gigabytes;
//                    double usedSpace = totalSpace - freeSpace;
//
//
//                    long percentage = 100;
//
//                    //calculate percentage here and add into map
//                    double freePercentage = ((totalSpace - usedSpace) / totalSpace * 100);
//                    double usedPercentage = ((totalSpace - freeSpace) / totalSpace * 100);
//
//
//                    double f = Double.parseDouble(df.format(freePercentage));
//                    double u = Double.parseDouble(df.format(usedPercentage));
//
//                    long finalPercentage = (long) (f+u);
//
//                    if(percentage==finalPercentage){
//                        map.put("pathName", root);
//                        map.put("totalSpace", (long) totalSpace + "GB");
//                        map.put("usedSpace", (long) usedSpace + "GB");
//                        map.put("freeSpace", (long) freeSpace + "GB");
//                        map.put("freeDataPercentage", f);
//                        map.put("usedDataPercentage", u);
//                    }else {
//                        map.put("pathName", root);
//                        map.put("totalSpace", (long) totalSpace + "GB");
//                        map.put("usedSpace", (long) usedSpace + "GB");
//                        map.put("freeSpace", (long) freeSpace + "GB");
//                        map.put("freeDataPercentage", f);
//                        map.put("usedDataPercentage", u);
//                    }
//
//
//                    return map;
//                }
//            }
//        }
//        catch (NullPointerException ns){
//            map.put("message","path is not exists in your system");
//            return map;
//        }
//        catch (Exception e) {
//            log.info(e+"");
//            e.printStackTrace();
//        }
//        return map;
//    }
    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }
    public static boolean isWindows() {
        return OS.contains("win");
    }

}
