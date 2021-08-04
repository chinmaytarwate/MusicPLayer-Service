// MusicService.aidl
package edu.chinmayt.cs478.musicCommon;

// Declare any non-default types here with import statements

interface MusicService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
      String getURLOfOne(in int urlID);
      Bundle getAll(in int id);
      Bundle getAllDetails();
}