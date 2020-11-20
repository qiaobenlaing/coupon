//
//  OpenClassUtil.h
//  BMSQS
//
//  Created by gh on 16/3/10.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface OpenClassUtil : NSObject

+ (UITextField *)ggsetTextField:(CGRect)frame tag:(int)tag placeholder:(NSString *)placeholder view:(UIView *)view;

+ (UILabel *)openClassSetLabel:(CGRect)frame text:(NSString *)text view:(UIView *)view;
+ (UILabel *)openClassSetLabel:(CGRect)frame text:(NSString *)text view:(UIView *)view tag:(int)labelTag;
+ (UILabel *)openClassSetLabel:(CGRect)frame text:(NSString *)text font:(UIFont *)font textColor:(UIColor *)textColor view:(UIView *)view;
//返回cell的高度
+ (CGSize)getOpenClassCellHeight:(NSString *)textStr ;
//返回info的高度
+ (CGSize)getInfolabelSize:(NSString *)textStr ;

+ (UITableView *)openClassSyllabusTableView:(CGRect)frame view:(UIScrollView *)view theDelegate:(id)delegate;

+ (NSString *)jsonStringWithArray:(NSArray *)array;

+ (NSString *)jsonStringWithDictionary:(NSMutableDictionary *)dic ;

//退款理由高度
+ (CGSize)getRefundReasonSize:(NSString *)textStr;

@end
