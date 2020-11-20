//
//  ActImageTextAttachment.h
//  BMSQS
//
//  Created by gh on 16/1/24.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ActImageTextAttachment : NSTextAttachment

@property (nonatomic, strong)NSString *imgUrl;
@property(assign, nonatomic) CGSize imgSize;



//homeViewController
@property(assign, nonatomic)CGRect imgframe;


@end
