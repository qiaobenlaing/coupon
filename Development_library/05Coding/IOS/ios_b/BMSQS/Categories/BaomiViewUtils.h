//
//  BaomiViewUtils.h
//  text
//
//  Created by liuqin on 15/10/31.
//  Copyright © 2015年 singlee.l. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>


@interface BaomiViewUtils : NSObject



#pragma mark --View--
+(UIView *)creatView:(CGRect)frame bgColor:(UIColor *)bgColor cornerRadius:(CGFloat)cornerRadius borderColor:(UIColor *)borderColor  borderWidth:(CGFloat)borderWidth tag:(int)tag;






#pragma mark --button--
// UIButton  frame title titleColor bgColor
+(UIButton *)creatBtn:(CGRect)frame title:(NSString *)btnTile titleColor:(UIColor *)titleColor bgColor:(UIColor *)bgColor tag:(int)tag;

// UIButton layer
+(UIButton *)creatBtn:(CGRect)frame title:(NSString *)btnTile titleColor:(UIColor *)titleColor bgColor:(UIColor *)bgColor cornerRadius:(CGFloat)cornerRadius borderColor:(UIColor *)borderColor  borderWidth:(CGFloat)borderWidth tag:(int)tag;
// UIButtonImage
+(UIButton *)creatbtn:(CGRect)frame StateNormalImage:(UIImage *)normalImage StateSelectedImage:(UIImage *)seleImage tag:(int)tag;



#pragma mark --Label--
//label
//+(UILabel *)creatLabel:(CGRect)frame;



#pragma mark --line--
//画一条分隔线
+(void)addLine:(UIView *)parent y:(CGFloat)y color:(UIColor *)bgColor;
+(void)addLine:(UIView *)parent y:(CGFloat)y;

+(UITextField *)creatTextField:(CGRect)frame bgColor:(UIColor *)bgColor  borderColor:(UIColor *)borderColor  borderWidth:(CGFloat)borderWidth tag:(int)tag cornerRadius:(CGFloat)cornerRadius placeholder:(NSString *)placeholderStr font:(UIFont *)font;
@end
