//
//  selectBtnObject.h
//  51youHui
//
//  Created by cq on 13-2-20.
//  Copyright (c) 2013年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>

/***********************
 选择按钮对象,包含title，tag，backimage
 **********************/
@interface selectBtnObject : NSObject
{
    NSString* title;
    int tag;
    int sequence;
    NSString* backImage; //选择后的高亮图片
    NSString* backImageNormal; //正常图片
}

@property(nonatomic,retain)id object;
@property(nonatomic,retain)NSString* title;
@property(nonatomic)int tag;
@property(nonatomic)int sequence;
@property(nonatomic,retain)NSString* backImage;
@property(nonatomic,retain)NSString* backImageNormal;
@end
