
package com.kali_corporation.healthfitnessplus.sevice.WalkandStep;

import android.content.pm.PackageManager;

import com.kali_corporation.healthfitnessplus.sevice.WalkandStep.services.AbstractStepDetectorService;
import com.kali_corporation.healthfitnessplus.sevice.WalkandStep.services.AccelerometerStepDetectorService;
import com.kali_corporation.healthfitnessplus.sevice.WalkandStep.services.HardwareStepDetectorService;
import com.kali_corporation.healthfitnessplus.sevice.WalkandStep.utils.AndroidVersionHelper;




public class Factory {



    public static Class<? extends AbstractStepDetectorService> getStepDetectorServiceClass(PackageManager pm){
        if(pm != null && AndroidVersionHelper.supportsStepDetector(pm)) {
            return HardwareStepDetectorService.class;
        }else{
            return AccelerometerStepDetectorService.class;
        }
    }
}
