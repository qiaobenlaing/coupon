//
//  BMSQ_PhotoViewController.h
//  BMSQC
//
//  Created by liuqin on 15/9/10.
//  Copyright (c) 2015年 djx. All rights reserved.
//


#import "BaseViewController.h"
#import "VPImageCropperViewController.h"
#import "PhotoScrollView.h"

@interface BMSQ_PhotoViewController :UIViewControllerEx < UIActionSheetDelegate, UINavigationControllerDelegate, UIImagePickerControllerDelegate, VPImageCropperDelegate, PhotoScrollDelegate>

@property (nonatomic, strong)NSString *shopCode;


@end
