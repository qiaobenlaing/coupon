//
//  DecorationShopViewController.h
//  BMSQS
//
//  Created by lxm on 15/8/1.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"
#import "EGOImageButton.h"
#import "VPImageCropperViewController.h"
#import "TPKeyboardAvoidingTableView.h"
#import "EGOImageView.h"
@interface DecorationShopViewController : BaseViewController<UINavigationControllerDelegate, UIImagePickerControllerDelegate, UIActionSheetDelegate,UITextViewDelegate,EGOImageButtonDelegate,VPImageCropperDelegate>
{
    __strong UITextView *_textView;
    __strong UILabel *_textLabel;
    
    __weak IBOutlet TPKeyboardAvoidingTableView *_tableView;
    
    NSMutableArray *_dataArray;
    
    BOOL _isTypeAddTopImage;
    EGOImageButton *_currentIndex;
    UIButton *_currentIndexB;
    int _delIndex;
    BOOL _isAdd;
    
    NSMutableArray *_inputArray;
    
}
@property (nonatomic, strong) EGOImageView *portraitImageView;
@end
