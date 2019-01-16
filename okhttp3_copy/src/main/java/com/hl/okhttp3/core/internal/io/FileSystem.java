package com.hl.okhttp3.core.internal.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okio.Okio;
import okio.Sink;
import okio.Source;

public interface FileSystem {
  /** The host machine's local file system. */
 FileSystem SYSTEM = new FileSystem() {
    @Override public Source source(File file) throws FileNotFoundException {
      return Okio.source(file);
    }

    @Override public Sink sink(File file) throws FileNotFoundException {
      try {
        return Okio.sink(file);
      } catch (FileNotFoundException e) {
        // Maybe the parent directory doesn't exist? Try creating it first.
        file.getParentFile().mkdirs();
        return Okio.sink(file);
      }
    }

    @Override public Sink appendingSink(File file) throws FileNotFoundException {
      try {
        return Okio.appendingSink(file);
      } catch (FileNotFoundException e) {
        // Maybe the parent directory doesn't exist? Try creating it first.
        file.getParentFile().mkdirs();
        return Okio.appendingSink(file);
      }
    }

    @Override public void delete(File file) throws IOException {
      // If delete() fails, make sure it's because the file didn't exist!
      if (!file.delete() && file.exists()) {
        throw new IOException("failed to delete " + file);
      }
    }

    @Override public boolean exists(File file) {
      return file.exists();
    }

    @Override public long size(File file) {
      return file.length();
    }

    @Override public void rename(File from, File to) throws IOException {
      delete(to);
      if (!from.renameTo(to)) {
        throw new IOException("failed to rename " + from + " to " + to);
      }
    }

    @Override public void deleteContents(File directory) throws IOException {
      File[] files = directory.listFiles();
      if (files == null) {
        throw new IOException("not a readable directory: " + directory);
      }
      for (File file : files) {
        if (file.isDirectory()) {
          deleteContents(file);
        }
        if (!file.delete()) {
          throw new IOException("failed to delete " + file);
        }
      }
    }
  };

  /** Reads from {@code file}. */
  Source source(File file) throws FileNotFoundException;

  /**
   * Writes to {@code file}, discarding any data already present. Creates parent directories if
   * necessary.
   */
  Sink sink(File file) throws FileNotFoundException;

  /**
   * Writes to {@code file}, appending if data is already present. Creates parent directories if
   * necessary.
   */
  Sink appendingSink(File file) throws FileNotFoundException;

  /** Deletes {@code file} if it exists. Throws if the file exists and cannot be deleted. */
  void delete(File file) throws IOException;

  /** Returns true if {@code file} exists on the file system. */
  boolean exists(File file);

  /** Returns the number of bytes stored in {@code file}, or 0 if it does not exist. */
  long size(File file);

  /** Renames {@code from} to {@code to}. Throws if the file cannot be renamed. */
  void rename(File from, File to) throws IOException;

  /**
   * Recursively delete the contents of {@code directory}. Throws an IOException if any file could
   * not be deleted, or if {@code dir} is not a readable directory.
   */
  void deleteContents(File directory) throws IOException;
}
