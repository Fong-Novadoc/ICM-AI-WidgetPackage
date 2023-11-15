package nl.novadoc.icm.widgets.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Logger {
    public static final int All = 5;
    public static final int DEBUG = 4;
    public static final int INFO = 3;
    public static final int WARN = 2;
    public static final int ERROR = 1;
    public static final int NONE = 0;
    
    private static final String[] levels = { "None", "Error", "Warn", "Info", "Debug" };
    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    
    private static Integer maxLogFiles = 5;

    private static Long defaultMaxLogFileSize = 10485760L;
    private static Long defaultLogFileSize = 0L;
    private static Integer defaultLogLevel = DEBUG;
    private static File defaultLogFile;
    private static File defaultLogFolder;
    
    private String logTag = "";
    
    private Long customMaxLogFileSize = null;
    private Long customLogFileSize = 0L;
    private Integer customLogLevel = null;
    private File customLogFolder = null;
    private File customLogFile = null;

    private Logger(String TAG) {
        this.logTag = TAG;
    }
    
    private Logger(String TAG, Integer customLogLevel) {
        this(TAG);
        this.customLogLevel = customLogLevel;
    }
    
    private Logger(String TAG, Integer customLogLevel, String customLogFolderName, String customLogFileName) {
        this(TAG, customLogLevel, customLogFolderName, customLogFileName, Logger.defaultMaxLogFileSize);
    }
    
    private Logger(String TAG, Integer customLogLevel, String customLogFolderName, String customLogFileName, Long customMaxLogFileSize) {
        this(TAG, customLogLevel);
        this.customLogFolder = createLogFolder(customLogFolderName);
        this.customLogFile = createLogFile(this.customLogFolder, customLogFileName);
        this.customLogFileSize = this.customLogFile.length();

        this.customMaxLogFileSize = customMaxLogFileSize;
    }
    
    /**
     * Get default Logger, like created with init
     * 
     * @param clazz
     * @return
     */
    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz.getSimpleName(), null);
    }

    /**
     * Get default logger, but with different log level
     * 
     * @param clazz
     * @param customLogLevel
     * @return
     */
    public static Logger getLogger(Class<?> clazz, Integer customLogLevel) {
        return new Logger(clazz.getSimpleName(), customLogLevel);
    }

    /**
     * Get new logger, with custom filename and location
     * 
     * @param clazz
     * @param customLogLevel
     * @param customLogFolderPath
     * @param customLogFileName
     * @return
     */
    public static Logger getLogger(Class<?> clazz, Integer customLogLevel, String customLogFolderPath, String customLogFileName) {
        return new Logger(clazz.getSimpleName(), customLogLevel, customLogFolderPath, customLogFileName, Logger.defaultMaxLogFileSize);
    }

    /**
     * Create a custom logger, with a custom filesize
     * 
     * @param clazz
     * @param customLogLevel
     * @param customLogFolderPath
     * @param customLogFileName
     * @param customMaxLogFileSize
     * @return
     */
    public static Logger getLogger(Class<?> clazz, Integer customLogLevel, String customLogFolderPath, String customLogFileName, Long customMaxLogFileSize) {
        return new Logger(clazz.getSimpleName(), customLogLevel, customLogFolderPath, customLogFileName, customMaxLogFileSize);
    }

    /**
     * Create a default logger based on a property file in the root of the jar
     * 
     * @param clazz
     * @param propertiesFileName
     * @return
     * @throws Exception
     */
    public static Logger init(Class<?> clazz, String propertiesFileName) throws Exception {
        InputStream is = null;
        
        try {
            is = Logger.class.getClassLoader().getResourceAsStream(propertiesFileName);

            Properties properties = new Properties();
            properties.load(is);
            
            return init(clazz, properties);
        } catch (Exception e) {
            throw e;
        } finally{
            if(is != null){
                try {
                    is.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }  
    }
    
    /**
     * Create a default logger with a java.util.Properties object 
     * 
     * @param clazz
     * @param properties
     * @return
     */
    public static Logger init(Class<?> clazz, Properties properties) {
        Logger.defaultLogFolder = createLogFolder(properties.getProperty("logpath", "c:/log"));
        Logger.defaultLogFile = createLogFile(Logger.defaultLogFolder, properties.getProperty("logname", "log"));
        Logger.defaultLogFileSize = Logger.defaultLogFile.length();
        
        Logger.defaultMaxLogFileSize = Long.parseLong(properties.getProperty("logfilesize", "10485760")); 
        Logger.maxLogFiles = Integer.parseInt(properties.getProperty("logfiles", "5"));
        Logger.defaultLogLevel = convertLevel(properties.getProperty("loglevel", "error"));

        Logger logger = new Logger(clazz.getSimpleName());
        
        return logger;
    }
    
    /**
     * Check and create the directories for the logger
     * 
     * @param directory
     * @return
     */
    private static File createLogFolder(String directory) {
        File logFolder = new File(directory);
        
        if (!logFolder.exists())
            logFolder.mkdirs();
        
        return logFolder;
    }
    
    /**
     * Creates tje logFile
     * 
     * @param logFolder
     * @param name
     * @return
     */
    private static File createLogFile(File logFolder, String name) {
        try {
            File logFile = new File(logFolder, name + ".log");
            logFile.createNewFile();
            
            return logFile;
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize Logger");
        }
    }
    
    /**
     * Return the loglevel int based on a string
     * 
     * @param level
     * @return
     */
    private static int convertLevel(String level) {
        level = level.toLowerCase();
        
        for (int i = 0; i < All; i++)
            if (level.equalsIgnoreCase(levels[i].toLowerCase()))
                return i;
        
        return DEBUG;
    }

    /**
     * returns the default loglevel
     * 
     * @return
     */
    public int getLogLevel() {
        return defaultLogLevel;
    }

    /**
     * retuns a custom loglevel for an instance
     * 
     * @return
     */
    public int getCustomLogLevel() {
        return customLogLevel;
    }

    /**
     * Sets a custom loglevel for a Logger instance
     * 
     * @param customLogLevel
     */
    public void setCustomLogLevel(int customLogLevel) {
        this.customLogLevel = customLogLevel;
    }
    
    public String debug(String message) {
        appendFile(DEBUG, message);
        return message;
    }

    public String[] debug(String[] messageArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        
        for(String message : messageArr) {
            sb.append(message);
            sb.append(" ");
        }
        
        sb.append("]");
        
        appendFile(DEBUG, sb.toString());
        
        return messageArr;
    }

    public Boolean debug(Boolean message) {
        appendFile(DEBUG, message);
        return message;
    }

    public Integer debug(Integer message) {
        appendFile(DEBUG, message);
        return message;
    }

    public Float debug(Float message) {
        appendFile(DEBUG, message);
        return message;
    }

    public Double debug(Double message) {
        appendFile(DEBUG, message);
        return message;
    }

    public Date debug(Date message) {
        appendFile(DEBUG, message);
        return message;
    }

    public void debug(Object ... message) {
        appendFile(DEBUG, message);
    }

    public void info(Object ... message) {
        appendFile(INFO, message);
    }

    public void warn(Object ... message) {
        appendFile(WARN, message);
    }

    public void error(Object ... message) {
        appendFile(ERROR, message);
    }
    
    /**
     * Checks the loglevel
     * 
     * @param logLevel
     * @return
     */
    private boolean checkLevel(int logLevel) {
        if(this.customLogLevel == null)
            return Logger.defaultLogLevel >= logLevel;
        else
            return this.customLogLevel >= logLevel;
    }

    /**
     * Appends the log message to file
     * 
     * @param level
     * @param objects
     */
    private void appendFile(int level, Object ... objects) {
        if (checkLevel(level))
            for(Object object : objects)
                if(object instanceof Throwable)
                    appendThrowable((Throwable) object, level, false);
                else
                    appendFile(object, level, true);
    }
    
    /**
     * Appenda a Throwable as one string to file
     * 
     * @param t
     * @param level
     * @param header
     */
    public void appendThrowable(Throwable t, int level, boolean header) {
        StringBuilder sb = new StringBuilder();
        sb.append(t.toString());
        sb.append("\r\n");
        
        for (StackTraceElement stackTraceEl : t.getStackTrace()) {
            sb.append("\t");
            sb.append(stackTraceEl.toString());
            sb.append("\r\n");
        }
    
        appendFile(sb.toString(), level, header);
    }

    /**
     * Appends the logfile
     * 
     * @param object
     * @param level
     * @param header
     * @return
     */
    private boolean appendFile(Object object, int level, boolean header) {
        return appendFile(object, level, header, true);
    }
    
    /**
     * Appends the logfile
     * 
     * @param object
     * @param level
     * @param header
     * @param crlf
     * @return
     */
    private boolean appendFile(Object object, int level, boolean header, boolean crlf) {
        Writer out = null;

        try {
            File logFile = checkRotate();
            StringBuilder sb = createLogMessage(object, level, header, crlf);
            
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), "utf-8"));
            out.write(sb.toString());
            out.flush();
            
            updateLogFileSize(sb.length());
            
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }
    
    /**
     * Create the actual log message (String)
     * 
     * @param object
     * @param level
     * @param header
     * @param crlf
     * @return
     */
    private StringBuilder createLogMessage(Object object, int level, boolean header, boolean crlf) {
        StringBuilder sb = new StringBuilder();

        if (header) {
            sb.append(format.format(new Date()));
            sb.append(" ");
            sb.append(levels[level]);
            sb.append("\t");
            sb.append(logTag);
        }
        
        sb.append("\t");
        sb.append(String.valueOf(object));
        
        if(crlf)
            sb.append("\r\n");
        
        return sb;
    }
    
    /**
     * Returns the defaultLogFile or if used the customLogFile
     * 
     * @return
     */
    private File getLogFile() {
        if(customLogFile == null)
            return defaultLogFile;
        else
            return customLogFile;
    }

    /**
     * Returns the defaultLogFileSize or if a customLogFile is used the customLogFileSize
     * 
     * @return
     */
    private Long getLogFileSize() {
        if(customLogFile == null)
            return defaultLogFileSize;
        else
            return customLogFileSize;
    }

    /**
     * Returns the defaultMaxLogFileSize or if a customLogFile is used the customMaxLogFileSize
     * 
     * @return
     */
    private Long getMaxLogFileSize() {
        if(customLogFile == null)
            return defaultMaxLogFileSize;
        else
            return customMaxLogFileSize;
    }

    /**
     * Adds new string length to the logFileSize
     * 
     * @param addition
     */
    private void updateLogFileSize(int addition) {
        updateLogFileSize(new Integer(addition));
    }
    
    /**
     * Adds new string length to the logFileSize
     * 
     * @param addition
     */
    private void updateLogFileSize(Integer addition) {
        updateLogFileSize(addition.longValue());
    }
    
    /**
     * Adds new string length to the logFileSize
     * 
     * @param addition
     */
    private void updateLogFileSize(Long addition) {
        if(customLogFile == null)
            defaultLogFileSize += addition;
        else
            customLogFileSize += addition;
    }
    
    /**
     * Sets logFileSize to 0L
     * @return
     */
    private Long resetLogFileSize() {
        if(customLogFile == null)
            return defaultLogFileSize = 0L;
        else
            return customLogFileSize = 0L;
    }
    
    /**
     * Checks if rotation is needed and executes rotation
     * 
     * @return
     */
    private File checkRotate() {
        File logFile = getLogFile();
        Long logFileSize = getLogFileSize();
        
        if(logFileSize > getMaxLogFileSize()) {
            rotateLogFiles(logFile);
            resetLogFileSize();
        }
        
        return logFile;
    }

    /**
     * Rotates the log file
     * 
     * @param file
     */
    private void rotateLogFiles(File file) {
        try {
            for(int i = maxLogFiles; i >= 0; --i) {
                String fileName = file.getPath()+"."+(i-1);
                
                if(checkFileExists(fileName)) {
                    if(i == maxLogFiles)
                        deleteFile(fileName);
                    else
                        renameFile(fileName, file.getPath()+"."+i);
                }
            }

            renameFile(file.getPath(), file.getPath()+".0");
            file.createNewFile();
            
            resetLogFileSize();
        } catch(Exception e) {
            System.err.println("Could not rotate logfiles");
        }
    }
    
    /**
     * Renames a serie of log files
     * 
     * @param oldFileName
     * @param newFileName
     */
    private void renameFile(String oldFileName, String newFileName) {
        File oldFile = new File(oldFileName);
        File newFile = new File(newFileName);
        
        if(!oldFile.renameTo(newFile))
            System.err.println("Rename of ("+oldFile.getPath()+") to ("+newFile.getPath()+") failed");
    }
    
    /**
     * Checks if the file exists
     * 
     * @param file
     * @return
     */
    private boolean checkFileExists(String file) {
        File f = new File(file);
        
        if(f.exists() && !f.isDirectory())
            return true;
        
        return false;
    }
    
    /**
     * deletes log file in tail
     * 
     * @param file
     */
    private void deleteFile(String file) {
        File f = new File(file);

        if(!f.delete())
            System.err.println("Delete operation of ("+f.getPath()+") failed.");
    }
}