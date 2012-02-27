// Copyright 2012 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.android.mwcnfc;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: trevorjohns
 * Date: 2/26/12
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Field {

    public String getValue();

    public void setValue(String value);

    public void readFromTag(NfcConnection nfcConnection) throws IOException;

}
