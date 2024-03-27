#!/bin/bash

#
# Copyright (c) 2024 Lunabee Studio
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# temp.sh
# Lunabee Compose
#
# Created by Lunabee Studio / Date - 3/27/2024 - for the Lunabee Compose library.
#

projects_string=$(./gradlew -q publishList | tail -n 1)
OLD_IFS=$IFS
IFS=";"
read -ra projects <<< "$projects_string"
IFS=$OLD_IFS

for project in "${projects[@]}"; do
  project_version_var="${project//-/_}"
  echo "$project_version_var"
  project_version_var="${project_version_var^^}"
#  ./gradlew "$project"SnapshotVersion -Pcounter=%build.counter% -PlibName="$project_version_var"+"_VERSION"
  echo "$project_version_var"
done

#./gradlew publish -Psigning.secretKeyRingFile=%PGP_KEY_PATH% -Psigning.keyId=%PGP_KEY_ID% -Psigning.password=%PGP_KEY_PASSWORD% -Ppublishing.url="https://s01.oss.sonatype.org/content/repositories/snapshots/"
