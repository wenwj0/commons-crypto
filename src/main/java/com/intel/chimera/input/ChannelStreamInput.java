/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intel.chimera.input;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/**
 * The ChannelStreamInput class takes an <code>InputStream</code> object which also 
 * implements <code>ReadableByteChannel</code> interface and wraps it as <code>Input</code> object 
 * acceptable by <code>CryptoInputStream</code>. The <code>ReadableByteChannel</code> interface
 * can be used to read data faster with <code>ByteBuffer</code> and only if when we find it not supported, 
 * we fall back to the stream only .
 */
public class ChannelStreamInput extends StreamInput {
  private ReadableByteChannel channel;
  boolean isChannelReadSupported = true;

  public ChannelStreamInput(
      InputStream inputStream,
      int bufferSize) {
    super(inputStream, bufferSize);
    this.channel = (ReadableByteChannel) inputStream;
  }

  @Override
  public int read(ByteBuffer dst) throws IOException {
    if (isChannelReadSupported) {
      try {
        return channel.read(dst);
      } catch (UnsupportedOperationException e) {
        isChannelReadSupported = false;
        return super.read(dst);
      }
    } else {
      return super.read(dst);
    }
  }
}
