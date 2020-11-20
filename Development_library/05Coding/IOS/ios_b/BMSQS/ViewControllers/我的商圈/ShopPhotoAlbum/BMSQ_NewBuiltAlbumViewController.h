//
//  BMSQ_NewBuiltAlbumViewController.h
//  BMSQS
//
//  Created by gh on 15/10/16.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"

@interface BMSQ_NewBuiltAlbumViewController : UIViewControllerEx <UITextFieldDelegate>

@property(nonatomic)BOOL isEdit;
@property(nonatomic,strong)NSDictionary *albumDic;

@end
