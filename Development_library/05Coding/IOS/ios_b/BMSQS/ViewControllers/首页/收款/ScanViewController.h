//
//  ScanViewController.h
//  BMSQS
//
//  Created by gh on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import <AVFoundation/AVFoundation.h>

@protocol ScanViewDelegate <NSObject>

- (void)getOptimalPay:(NSDictionary *)dic bankCode:(NSString *)bankCode;


@end


@interface ScanViewController : UIViewControllerEx<AVCaptureMetadataOutputObjectsDelegate>
{
    int num;
    BOOL upOrdown;
    NSTimer * timer;
    AVCaptureSession * _AVSession;//调用闪光灯的时候创建的类
    BOOL isFlash;
}

@property (strong,nonatomic)id<ScanViewDelegate> scanDelegate;

@property (strong,nonatomic)AVCaptureDevice * device;
@property (strong,nonatomic)AVCaptureDeviceInput * input;
@property (strong,nonatomic)AVCaptureMetadataOutput * output;
@property (strong,nonatomic)AVCaptureSession * session;
@property (strong,nonatomic)AVCaptureVideoPreviewLayer * preview;
@property (nonatomic,strong)AVCaptureSession * AVSession;

@property (nonatomic, retain) UIImageView * line;

@end
