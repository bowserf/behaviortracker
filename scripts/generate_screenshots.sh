#!/bin/sh

BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
PROJECT_DIR="$BASE_DIR/.."

BUILD_DIR="$PROJECT_DIR/base/build"
DIR_SCREENSHOTS="$BUILD_DIR/app_screenshots"

echo "Delete previous screenshots dir"
rm -r $DIR_SCREENSHOTS

echo "Enable Demo mode"
./android_demo_mode.sh on 1100

pushd "$PROJECT_DIR" || exit
  ./gradlew connectedAndroidTest
popd || exit

echo "Disable Demo mode"
./android_demo_mode.sh off 1100

echo "Pull screenshost from device"
adb pull "/sdcard/googletest/test_outputfiles" $BUILD_DIR

mv "$BUILD_DIR/test_outputfiles" $DIR_SCREENSHOTS

echo "Screenshots available here: $DIR_SCREENSHOTS"