//
//  BMSQ_homeTypeCell.h
//  BMSQC
//
//  Created by liuqin on 15/12/4.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>



@protocol BMSQ_homeTypeCellDelegate <NSObject>

-(void)clickType:(int)tag typeDic:(NSDictionary *)dic;
@end



@interface BMSQ_homeTypeView : UIView

@property (nonatomic, strong)UIImageView *typeImage;
@property (nonatomic, strong)UILabel *typeLabel;

@property (nonatomic, strong)NSDictionary *typeDic;


-(void)setTypeV:(NSURL *)ImageUrl label:(NSString *)title;
@end


@interface BMSQ_homeTypeCell : UITableViewCell
@property (nonatomic, assign)BOOL isRowZero;


-(void)setHomeTypeCell:(id)reponse height:(float)height;

@property (nonatomic, weak)id<BMSQ_homeTypeCellDelegate> typeDelegate;
@end
