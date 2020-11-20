//
//  OpenClassUtil.m
//  BMSQS
//
//  Created by gh on 16/3/10.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "OpenClassUtil.h"

@implementation OpenClassUtil

//创建 textfield
+ (UITextField *)ggsetTextField:(CGRect)frame tag:(int)tag placeholder:(NSString *)placeholder view:(UIView *)view
{
    UITextField *textField = [[UITextField alloc] initWithFrame:frame];
    textField.layer.borderWidth = 1.0;
    textField.layer.borderColor = APP_CELL_LINE_COLOR.CGColor;
    textField.tag = tag;
    textField.placeholder = placeholder;
    
    textField.font = [UIFont systemFontOfSize:13.f];
    
    
    [view addSubview:textField];
    
    return textField;
}

+ (UILabel *)openClassSetLabel:(CGRect)frame text:(NSString *)text view:(UIView *)view
{
    UILabel *label = [[UILabel alloc] initWithFrame:frame];
    label.font = [UIFont systemFontOfSize:13.f];
    label.text = text;
    [view addSubview:label];
    return label;
}

+ (UILabel *)openClassSetLabel:(CGRect)frame text:(NSString *)text font:(UIFont *)font textColor:(UIColor *)textColor view:(UIView *)view {
    UILabel *label = [self openClassSetLabel:frame text:text view:view];
    
    if (textColor == nil) {
        label.textColor = [UIColor blackColor];
    }else {
        label.textColor = textColor;
    }
    label.font = font;
    return label;
}



+ (UILabel *)openClassSetLabel:(CGRect)frame text:(NSString *)text view:(UIView *)view tag:(int)labelTag{
    UILabel *label = [self openClassSetLabel:frame text:text view:view];
    label.tag = labelTag;
    return label;
    
}
//课程详情高度
+ (CGSize)getInfolabelSize:(NSString *)textStr {

    CGSize rSize = [textStr boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH/3*2-20, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:13.f]} context:nil].size;

    return rSize;

}

//退款理由  高度
+ (CGSize)getRefundReasonSize:(NSString *)textStr {
    CGSize rSize = [textStr boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH/4*3-10, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:13.f]} context:nil].size;
    
    return rSize;
}


//课程表tableview
+ (UITableView *)openClassSyllabusTableView:(CGRect)frame view:(UIScrollView *)view theDelegate:(id)delegate {
    
    UITableView *tableView = [[UITableView alloc] initWithFrame:frame];
    tableView.bounces = NO;
    tableView.showsVerticalScrollIndicator = NO;
    tableView.dataSource = delegate;
    tableView.delegate = delegate;
    tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [view addSubview:tableView];
    return tableView;
    
}

//array 转json
+ (NSString *)jsonStringWithArray:(NSArray *)array {
    NSString *jsonString ;
    if (array.count > 0) {
        NSError *err;
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:array
                                                           options:NSJSONWritingPrettyPrinted
                                                             error:&err];
        if (err) {
            //            NSLog(@"json解析失败：%@",err);
            return nil;
        }
        
        jsonString = [[NSString alloc] initWithData:jsonData
                                           encoding:NSUTF8StringEncoding];
        
    }else {
        jsonString = @"";
    }
    
    return jsonString;
}

+ (NSString *)jsonStringWithDictionary:(NSMutableDictionary *)dic {
    NSString *jsonString ;
    if (dic.count > 0) {
        NSError *err;
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dic
                                                           options:NSJSONWritingPrettyPrinted
                                                             error:&err];
        if (err) {
            //            NSLog(@"json解析失败：%@",err);
            return nil;
        }
        
        jsonString = [[NSString alloc] initWithData:jsonData
                                           encoding:NSUTF8StringEncoding];
        
    }else {
        jsonString = @"";
    }
    
    return jsonString;
}



@end
