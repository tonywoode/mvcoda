package themes;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.regex.Pattern;

/*
 * http://www.codetreat.com/find-a-file-in-directory-including-sub-directories-with-java-7-walkfiletree-java-example/
 * and
 * http://stackoverflow.com/questions/2534632/list-all-files-from-a-directory-recursively-with-java
 */
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;


//http://www.codetreat.com/find-a-file-in-directory-including-sub-directories-with-java-7-walkfiletree-java-example/.

class FindAfile implements FileVisitor
{
    private Path searchThisFile;
    Path searchMe = Paths.get("Urban.xml");
    private Pattern pattern = Pattern.compile(".*3.*");
    boolean matches = false;

    public FileVisitResult preVisitDirectory(Object dir,BasicFileAttributes attrs) throws IOException
    {
        System.out.println("directory to be visited next "+(Path)dir);
         matches = pattern.matcher(dir.toString()).matches();
        return FileVisitResult.CONTINUE;
    }


    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException
    {      
            search((Path)file);
             matches = pattern.matcher(file.toString()).matches();
            return FileVisitResult.CONTINUE;
    }      


    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException
    {  
        System.out.println((Path)file);
         matches = pattern.matcher(file.toString()).matches();
        return FileVisitResult.CONTINUE;
    }      


    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException
    {
        System.out.println("finished with "+(Path)dir);
         matches = pattern.matcher(dir.toString()).matches();
        return FileVisitResult.CONTINUE;
    }

    public boolean search(Path file) throws IOException
    {
        Path name = file.getFileName();
        System.out.println("current file is " + name);
        if (matches) { System.out.println("YES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! "); }
        if (name != null && name.equals(searchMe))
        {
            System.out.println("Searched file was found: " + searchThisFile +
                    " in " + file.toRealPath().toString());
            
                   return true;
        }
        else
            return false;

    }

    public void searchThis(Path searchedFile)
    {
        this.searchThisFile = searchedFile;      

    }    


}

public class WalkFileStructure
{

    public static void main(String args[]) throws IOException
    {
        Path digThisDirectory = Theme.getRootDir();
        Path searchMe = Paths.get("Classic.xml");
        FindAfile digDirectoryHelper = new FindAfile();
        digDirectoryHelper.searchThis(searchMe);
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        Files.walkFileTree(digThisDirectory, opts, Integer.MAX_VALUE, digDirectoryHelper);

    }

}



