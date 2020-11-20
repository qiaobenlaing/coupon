//
//  BMSQ_PersonInfoController.h
//  BMSQC
//
//  Created by djx on 15/8/1.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "BMSQ_PersonInfoCell.h"
#import "AFURLSessionManager.h"
#import "DAProgressOverlayView.h"
#import "VPImageCropperViewController.h"
#import "AFURLResponseSerialization.h"
#import "AFURLSessionManager.h"

#define ORIGINAL_MAX_WIDTH 640.0f

@interface BMSQ_PersonInfoController : UIViewControllerEx<UIActionSheetDelegate,UITextFieldDelegate,PersonInfoCellDelegate,UINavigationControllerDelegate,UIImagePickerControllerDelegate,VPImageCropperDelegate>

@end
