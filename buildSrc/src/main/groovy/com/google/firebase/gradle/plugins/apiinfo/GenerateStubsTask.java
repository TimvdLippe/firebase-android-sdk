// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.gradle.plugins.apiinfo;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

public abstract class GenerateStubsTask extends DefaultTask {
  @Input
  public abstract String getMetalavaJarPath();

  public abstract void setMetalavaJarPath(String path);

  @InputFiles
  public abstract Collection<File> getSourceDirs();

  public abstract void setSourceDirs(Collection<File> dirs);

  @OutputDirectory
  public abstract File getOutputDir();

  public abstract void setOutputDir(File dir);

  @TaskAction
  public void run() {
    String sourcePath =
        getSourceDirs().stream().map(File::getAbsolutePath).collect(Collectors.joining(":"));

    getProject()
        .javaexec(
            spec -> {
              spec.setMain("-jar");
              spec.setArgs(
                  Arrays.asList(
                      getMetalavaJarPath(),
                      "--quiet",
                      "--source-path",
                      sourcePath,
                      "--include-annotations",
                      "--doc-stubs",
                      getOutputDir().getAbsolutePath()));
            });
  }
}
