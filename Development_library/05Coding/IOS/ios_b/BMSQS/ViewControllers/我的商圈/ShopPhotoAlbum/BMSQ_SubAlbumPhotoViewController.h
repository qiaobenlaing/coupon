//
//  BMSQ_SubAlbumPhotoViewController.h
//  BMSQC
//
//  Created by liuqin on 15/9/11.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"
#import "PhotoScrollView.h"

@interface BMSQ_SubAlbumPhotoViewController : UIViewControllerEx <UIActionSheetDelegate, UINavigationControllerDelegate, UIImagePickerControllerDelegate, PhotoScrollDelegate>


@property (nonatomic, strong)NSString *myTitle;

@property (nonatomic, strong)NSDictionary *proDic;
@property (nonatomic, strong)NSString *code;

@end
