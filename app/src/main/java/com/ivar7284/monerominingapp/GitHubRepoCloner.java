package com.ivar7284.monerominingapp;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;

public class GitHubRepoCloner {
//    must add the following to the gradle
//    <dependency>
//        <groupId>org.eclipse.jgit</groupId>
//        <artifactId>org.eclipse.jgit</artifactId>
//        <version>5.14.0.202012080955-r</version>
//    </dependency>
//        implementation 'org.eclipse.jgit:org.eclipse.jgit:5.14.0.202012080955-r'


    public static void main(String[] args) {
        String repoUrl = ""; // Replace this with the GitHub repo link
        String targetDirectory = ""; // Replace this with the desired directory

        cloneRepository(repoUrl, targetDirectory);
    }

    public static void cloneRepository(String repoUrl, String targetDirectory) {
        try {
            File localPath = new File(targetDirectory);
            if (!localPath.exists()) {
                localPath.mkdirs();
            }
            System.out.println("Cloning into: " + localPath);

            CloneCommand cloneCommand = Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(localPath);
            Git git = cloneCommand.call();

            System.out.println("Repository cloned successfully.");
        } catch (GitAPIException e) {
            System.out.println("Failed to clone the repository: " + e.getMessage());
        }
    }
}

