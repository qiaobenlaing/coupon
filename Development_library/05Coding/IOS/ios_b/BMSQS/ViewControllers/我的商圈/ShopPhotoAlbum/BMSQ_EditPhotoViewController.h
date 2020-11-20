//
//  BMSQ_EditPhotoProduct.h
//  BMSQS
//
//  Created by gh on 15/10/19.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"
#import "BMSQ_SelectSubAlbumViewController.h"
#import "EGOImageButton.h"
#import "VPImageCropperViewController.h"
#import "TPKeyboardAvoidingTableView.h"
#import "EGOImageView.h"


@interface BMSQ_EditPhotoViewController : UIViewControllerEx <UINavigationControllerDelegate, UITableViewDataSource,UITableViewDelegate, EditPhotoDelegate> {
    

    
}

@property(nonatomic)BOOL isProduct;//1产品  0环境
@property(nonatomic)BOOL isUpload; //是否是上传

@property(nonatomic,strong)NSString *productName;
@property(nonatomic,strong)UIImage *image;
@property(nonatomic,strong)NSDictionary* dic;

@property(nonatomic,strong)NSDictionary *editDic;



@end
