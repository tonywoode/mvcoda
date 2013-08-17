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


class FindAfile implements FileVisitor
{
    private Path searchThisFile;

    public FileVisitResult preVisitDirectory(Object dir,BasicFileAttributes attrs) throws IOException
    {
        System.out.println("directory to be visited next "+(Path)dir);
        return FileVisitResult.CONTINUE;
    }


    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException
    {      
            search((Path)file);
            return FileVisitResult.CONTINUE;
    }      


    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException
    {  
        System.out.println((Path)file);
        return FileVisitResult.CONTINUE;
    }      


    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException
    {
        System.out.println("finished with "+(Path)dir);
        return FileVisitResult.CONTINUE;
    }

    public boolean search(Path file) throws IOException
    {
        Path name = file.getFileName();
        if (name != null && name.equals(searchThisFile))
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



