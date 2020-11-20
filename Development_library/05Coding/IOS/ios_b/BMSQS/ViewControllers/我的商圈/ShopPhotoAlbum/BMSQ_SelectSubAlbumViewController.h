//
//  BMSQ_SelectSubAlbumViewController.h
//  BMSQS
//
//  Created by gh on 15/10/20.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"


@protocol EditPhotoDelegate <NSObject>

- (void)setAlbumPhotoDic:(NSDictionary *)dictionary;

@end

@interface BMSQ_SelectSubAlbumViewController : UIViewControllerEx<UINavigationControllerDelegate,UITableViewDataSource, UITableViewDelegate>

@property(nonatomic,assign) NSObject<EditPhotoDelegate> *delegate;

@end
