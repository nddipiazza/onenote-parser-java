package org.apache.tika.onenote;

import java.util.ArrayList;
import java.util.List;

public class PropertyValue {
  PropertyID propertyID = new PropertyID();
  // union of one of these things based on the type of the corresponding PropertyID
  long scalar; // holds a boolean value if type = 0x2, retrieved from header
  // either ObjectID or ObjectSpaceID or ContextID (single value in array)
  // either ArrayOfObjectIDs or ArrayOfObjectSpaceIDs or ArrayOfContextID
  List<CompactID> compactIDs = new ArrayList<>();
  PropertySet propertySet = new PropertySet(); // or used to house a single value
  FileChunkReference rawData = new FileChunkReference(); // FourBytesOfLengthFollowedByData

  public void print(OneNote document) {
    // TODO
  }

  public PropertyID getPropertyID() {
    return propertyID;
  }

  public PropertyValue setPropertyID(PropertyID propertyID) {
    this.propertyID = propertyID;
    return this;
  }

  public long getScalar() {
    return scalar;
  }

  public PropertyValue setScalar(long scalar) {
    this.scalar = scalar;
    return this;
  }

  public List<CompactID> getCompactIDs() {
    return compactIDs;
  }

  public PropertyValue setCompactIDs(List<CompactID> compactIDs) {
    this.compactIDs = compactIDs;
    return this;
  }

  public PropertySet getPropertySet() {
    return propertySet;
  }

  public PropertyValue setPropertySet(PropertySet propertySet) {
    this.propertySet = propertySet;
    return this;
  }

  public FileChunkReference getRawData() {
    return rawData;
  }

  public PropertyValue setRawData(FileChunkReference rawData) {
    this.rawData = rawData;
    return this;
  }
}
