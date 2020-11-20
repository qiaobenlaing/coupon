//
//  AccountListCell.m
//  BMSQS
//
//  Created by gh on 16/2/24.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "AccountListCell.h"
#import "UIImageView+WebCache.h"

@interface AccountListCell () {
    int cellTag;
    UIView *accountView;
    
    NSDictionary *data;
    
}

@end

@implementation AccountListCell

- (void)configUI:(int)viewTag value:(NSDictionary *)dic {
    if (accountView) {
        [accountView removeFromSuperview];
        accountView = nil;
    }
    cellTag = viewTag;
    data = dic;
    [self setViewUp];
    
    
    
}

//:(NSDictionary *)dic
- (void)setViewUp{

    switch (cellTag) {
            
        case 1000: //账单查询
            [self queryList];
            break;
        case 1100: //顾客清单
             [self customerList];
            break;
        case 1101: //消费未结算账单
            [self unfinishedList];
            break;
        case 1102: //支付结算对账
            [self balanceList];
            break;
        case 1200: //退款清单
            [self drawbackList];
            break;
        case 1201: //补贴未结算账单
            [self unfinishedList];
            break;
        case 1202: //补贴结算对账
            [self balanceList];
            break;
        default:
            break;
    }
    
    
    
}

- (void)queryList {
    CGFloat originX = 80;
    CGFloat originY = 0;
    CGFloat sizeWidth = (APP_VIEW_WIDTH-80)/2;
    CGFloat sizeHeight = 20;
    
    accountView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 101)];
    accountView.backgroundColor = [UIColor clearColor];
    [self.contentView addSubview:accountView];
    //头像
    UIImageView *iv = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, 60, 60)];
    [iv sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL, [data objectForKey:@"avatarUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    [accountView addSubview:iv];
    
    //昵称
    UILabel *nickLabel = [self setAccountLabel: CGRectMake(originX, originY, sizeWidth, sizeHeight)];
    nickLabel.text = [NSString stringWithFormat:@"%@",[data objectForKey:@"nickName"]];
    [accountView addSubview:nickLabel];
    
    //手机号后四位
    UILabel *mobileNbrLabel = [self setAccountLabel: CGRectMake(originX+sizeWidth, originY, sizeWidth, sizeHeight)];
    mobileNbrLabel.text = [NSString stringWithFormat:@"手机号后四位:%@",[data objectForKey:@"mobileNbr"]];
    [accountView addSubview:nickLabel];
    
    originY = originY + sizeHeight;
    //消费金额
    UILabel *consumptionAmountLabel = [self setAccountLabel: CGRectMake(originX , originY, sizeWidth, sizeHeight)];
    consumptionAmountLabel.text = [NSString stringWithFormat:@"消费:%.2f元",[[data objectForKey:@"consumptionAmount"]floatValue]];
    [accountView addSubview:consumptionAmountLabel];
    
    originY = originY + sizeHeight;
    //支付
    UILabel *payAmountLabel = [self setAccountLabel: CGRectMake(originX , originY, sizeWidth, sizeHeight)];
    payAmountLabel.text = [NSString stringWithFormat:@"支付:%.2f元", [[data objectForKey:@"payAmount"]floatValue]];
    [accountView addSubview:payAmountLabel];
    
    //优惠
    UILabel *discountAmountLabel = [self setAccountLabel: CGRectMake(originX + sizeWidth, originY, sizeWidth, sizeHeight)];
    discountAmountLabel.text = [NSString stringWithFormat:@"优惠:%.2f",[[data objectForKey:@"discountAmount"]floatValue]];
    [accountView addSubview:discountAmountLabel];
    
    
    
    sizeWidth = (APP_VIEW_WIDTH - 90);
    originY = originY + sizeHeight;
    //消费时间
    UILabel *consumptionTimeLabel = [self setAccountLabel: CGRectMake(originX , originY, sizeWidth, sizeHeight)];
    consumptionTimeLabel.text = [NSString stringWithFormat:@"消费时间:%.@",[data objectForKey:@"consumptionTime"]];
    [accountView addSubview:consumptionTimeLabel];
    
    originY = originY + sizeHeight;
    //订单号
    UILabel *orderNbrLabel = [self setAccountLabel: CGRectMake(originX , originY, sizeWidth, sizeHeight)];
    orderNbrLabel.text = [NSString stringWithFormat:@"订单号:%.@",[data objectForKey:@"orderNbr"]];
    orderNbrLabel.textAlignment = NSTextAlignmentRight;
    [accountView addSubview:orderNbrLabel];
    
    UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(0, accountView.frame.size.height-1, APP_VIEW_WIDTH, 1)];
    lineView.backgroundColor = APP_CELL_LINE_COLOR;
    [accountView addSubview:lineView];

    
}


- (void)balanceList {
    CGFloat originX = 80;
    CGFloat originY = 0;
    CGFloat sizeWidth = (APP_VIEW_WIDTH-80)/2;
    CGFloat sizeHeight = 20;
    
    
    accountView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 81)];
    accountView.backgroundColor = [UIColor clearColor];
    [self.contentView addSubview:accountView];
    
    if (cellTag == 1202) {
        CGRect actFrame = accountView.frame;
        actFrame.size = CGSizeMake(APP_VIEW_WIDTH, 101);
        accountView.frame = actFrame;
    }
    
    
    UIImageView *iv = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, 60, 60)];
    [iv sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL, [data objectForKey:@"avatarUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    //    iv.backgroundColor = [UIColor b];
    [accountView addSubview:iv];
    
    //昵称
    UILabel *nickLabel = [self setAccountLabel: CGRectMake(originX, originY, sizeWidth, sizeHeight)];
    nickLabel.text = [NSString stringWithFormat:@"%@",[data objectForKey:@"nickName"]];
    [accountView addSubview:nickLabel];
    
    //手机号后四位
    UILabel *mobileNbrLabel = [self setAccountLabel: CGRectMake(originX+sizeWidth, originY, sizeWidth, sizeHeight)];
    mobileNbrLabel.text = [NSString stringWithFormat:@"手机号后四位:%@",[data objectForKey:@"mobileNbr"]];
    [accountView addSubview:nickLabel];
    
    originY = originY + sizeHeight;
    //消费金额
    UILabel *consumptionAmountLabel = [self setAccountLabel: CGRectMake(originX , originY, sizeWidth, sizeHeight)];
    consumptionAmountLabel.text = [NSString stringWithFormat:@"消费:%.2f元",[[data objectForKey:@"consumptionAmount"]floatValue]];
    [accountView addSubview:consumptionAmountLabel];
    //支付
    UILabel *payAmountLabel = [self setAccountLabel: CGRectMake(originX+sizeWidth , originY, sizeWidth, sizeHeight)];
    payAmountLabel.text = [NSString stringWithFormat:@"支付:%.2f元", [[data objectForKey:@"payAmount"]floatValue]];
    [accountView addSubview:payAmountLabel];
    
    //   1202
    if (cellTag == 1202) {
        originY = originY + sizeHeight;
        UILabel *subsidyAmountLabel = [self setAccountLabel: CGRectMake(originX+sizeWidth , originY, sizeWidth, sizeHeight)];
        subsidyAmountLabel.text = [NSString stringWithFormat:@"补贴:%.2f元", [[data objectForKey:@"subsidyAmount"]floatValue]];
        [accountView addSubview:subsidyAmountLabel];
        
    }
    sizeWidth = (APP_VIEW_WIDTH - 90);
    originY = originY + sizeHeight;
    //消费时间
    UILabel *consumptionTimeLabel = [self setAccountLabel: CGRectMake(originX , originY, sizeWidth, sizeHeight)];
    consumptionTimeLabel.text = [NSString stringWithFormat:@"消费时间:%.@",[data objectForKey:@"consumptionTime"]];
    [accountView addSubview:consumptionTimeLabel];
    
    originY = originY + sizeHeight;
    //订单号
    UILabel *orderNbrLabel = [self setAccountLabel: CGRectMake(originX , originY, sizeWidth, sizeHeight)];
    orderNbrLabel.text = [NSString stringWithFormat:@"订单号:%.@",[data objectForKey:@"orderNbr"]];
    orderNbrLabel.textAlignment = NSTextAlignmentRight;
    [accountView addSubview:orderNbrLabel];
    
    UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(0, accountView.frame.size.height-1, APP_VIEW_WIDTH, 1)];
    lineView.backgroundColor = APP_CELL_LINE_COLOR;
    [accountView addSubview:lineView];
    
    
}





//1101 1201
- (void)unfinishedList {
    accountView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 81)];
    accountView.backgroundColor = [UIColor clearColor];
    [self.contentView addSubview:accountView];
    
    if (cellTag == 1201) {
        CGRect actFrame = accountView.frame;
        actFrame.size = CGSizeMake(APP_VIEW_WIDTH, 101);
        accountView.frame = actFrame;
    }
    
    
    
    UIImageView *iv = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, 60, 60)];
    [iv sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL, [data objectForKey:@"avatarUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    //    iv.backgroundColor = [UIColor b];
    [accountView addSubview:iv];
    
    CGFloat originX = 80;
    CGFloat originY = 0;
    CGFloat sizeWidth = (APP_VIEW_WIDTH-80)/2;
    CGFloat sizeHeight = 20;
    
    //昵称
    UILabel *nickLabel = [self setAccountLabel: CGRectMake(originX, originY, sizeWidth, sizeHeight)];
    nickLabel.text = [NSString stringWithFormat:@"%@",[data objectForKey:@"nickName"]];
    [accountView addSubview:nickLabel];
    
    //手机号后四位
    UILabel *mobileNbrLabel = [self setAccountLabel: CGRectMake(originX+sizeWidth, originY, sizeWidth, sizeHeight)];
    mobileNbrLabel.text = [NSString stringWithFormat:@"手机号后四位:%@",[data objectForKey:@"mobileNbr"]];
    [accountView addSubview:nickLabel];
    
    originY = originY + sizeHeight;
    //消费金额 支付金额
    UILabel *consumptionAmountLabel = [self setAccountLabel: CGRectMake(originX , originY, sizeWidth, sizeHeight)];
    consumptionAmountLabel.text = [NSString stringWithFormat:@"消费:%.2f元",[[data objectForKey:@"consumptionAmount"]floatValue]];
    [accountView addSubview:consumptionAmountLabel];
    
    UILabel *payAmountLabel = [self setAccountLabel: CGRectMake(originX+sizeWidth , originY, sizeWidth, sizeHeight)];
    payAmountLabel.text = [NSString stringWithFormat:@"支付:%.2f元", [[data objectForKey:@"payAmount"]floatValue]];
    [accountView addSubview:payAmountLabel];
    
    if (cellTag == 1201) {
        originY = originY + sizeHeight;
        //补贴
        UILabel *refundAmountLabel = [self setAccountLabel: CGRectMake(originX, originY, sizeWidth, sizeHeight)];
        refundAmountLabel.text = [NSString stringWithFormat:@"补贴:%.2f元", [[data objectForKey:@"subsidyAmount"]floatValue]];
        [accountView addSubview:refundAmountLabel];
        
    }
    
    sizeWidth = (APP_VIEW_WIDTH - 90);
    originY = originY + sizeHeight;
    //消费时间
    UILabel *consumptionTimeLabel = [self setAccountLabel: CGRectMake(originX , originY, sizeWidth, sizeHeight)];
    consumptionTimeLabel.text = [NSString stringWithFormat:@"消费时间:%.@",[data objectForKey:@"consumptionTime"]];
    [accountView addSubview:consumptionTimeLabel];
    
    originY = originY + sizeHeight;
    //订单号
    UILabel *orderNbrLabel = [self setAccountLabel: CGRectMake(originX , originY, sizeWidth, sizeHeight)];
    orderNbrLabel.text = [NSString stringWithFormat:@"订单号:%.@",[data objectForKey:@"orderNbr"]];
    orderNbrLabel.textAlignment = NSTextAlignmentRight;
    [accountView addSubview:orderNbrLabel];
    
    UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(0, accountView.frame.size.height-1, APP_VIEW_WIDTH, 1)];
    lineView.backgroundColor = APP_CELL_LINE_COLOR;
    [accountView addSubview:lineView];
    
}


//退款清单
- (void)drawbackList {
    accountView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 121)];
    accountView.backgroundColor = [UIColor clearColor];
    [self.contentView addSubview:accountView];
    
    UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(0, 120, APP_VIEW_WIDTH, 1)];
    lineView.backgroundColor = APP_CELL_LINE_COLOR;
    [accountView addSubview:lineView];
    
    UIImageView *iv = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, 60, 60)];
    [iv sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL, [data objectForKey:@"avatarUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    [accountView addSubview:iv];
    
    CGFloat originX = 80;
    CGFloat originY = 0;
    CGFloat sizeWidth = (APP_VIEW_WIDTH-80)/2;
    CGFloat sizeHeight = 100/5;
    
    //昵称
    UILabel *nickLabel = [self setAccountLabel: CGRectMake(originX, originY, APP_VIEW_WIDTH/2, sizeHeight)];
    nickLabel.text = [NSString stringWithFormat:@"%@",[data objectForKey:@"nickName"]];
    [accountView addSubview:nickLabel];
    
    //手机号后四位
    UILabel *mobileNbrLabel = [self setAccountLabel: CGRectMake(originX+sizeWidth, originY, sizeWidth, sizeHeight)];
    mobileNbrLabel.text = [NSString stringWithFormat:@"手机号后四位:%@",[data objectForKey:@"mobileNbr"]];
    [accountView addSubview:nickLabel];
    
    originY = originY + sizeHeight;
    //消费金额
    UILabel *consumptionAmountLabel = [self setAccountLabel: CGRectMake(originX , originY, sizeWidth, sizeHeight)];
    consumptionAmountLabel.text = [NSString stringWithFormat:@"消费:%.2f",[[data objectForKey:@"consumptionAmount"]floatValue]];
    [accountView addSubview:consumptionAmountLabel];
    
    //支付金额
    UILabel *payAmountLabel = [self setAccountLabel: CGRectMake(originX + sizeWidth, originY, sizeWidth, sizeHeight)];
    payAmountLabel.text = [NSString stringWithFormat:@"支付:%.2f",[[data objectForKey:@"payAmount"]floatValue]];
    [accountView addSubview:payAmountLabel];
    
    originY = originY + sizeHeight;
    //退款
    UILabel *refundAmountLabel = [self setAccountLabel: CGRectMake(originX , originY, sizeWidth, sizeHeight)];
    refundAmountLabel.text = [NSString stringWithFormat:@"退款:%.2f",[[data objectForKey:@"payAmount"]floatValue]];
    [accountView addSubview:refundAmountLabel];
    
    sizeWidth = (APP_VIEW_WIDTH - 90);
    
    //消费时间
    originY = originY + sizeHeight;
    UILabel *consumptionTimeLabel = [self setAccountLabel: CGRectMake(originX , originY, sizeWidth, sizeHeight)];
    consumptionTimeLabel.text = [NSString stringWithFormat:@"消费时间:%.@",[data objectForKey:@"consumptionTime"]];
    [accountView addSubview:consumptionTimeLabel];
    
    originY = originY + sizeHeight;
    //退款时间
    UILabel *refundTimeLabel = [self setAccountLabel: CGRectMake(originX , originY, sizeWidth, sizeHeight)];
    refundTimeLabel.text = [NSString stringWithFormat:@"退款时间:%.@",[data objectForKey:@"refundTime"]];
    [accountView addSubview:refundTimeLabel];

    originY = originY + sizeHeight;
    //订单号
    UILabel *orderNbrLabel = [self setAccountLabel: CGRectMake(originX , originY, sizeWidth, sizeHeight)];
    orderNbrLabel.text = [NSString stringWithFormat:@"订单号:%.@",[data objectForKey:@"orderNbr"]];
    orderNbrLabel.textAlignment = NSTextAlignmentRight;
    [accountView addSubview:orderNbrLabel];
    
}

//顾客清单 1
- (void)customerList {
    accountView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 81)];
    accountView.backgroundColor = [UIColor clearColor];
    [self.contentView addSubview:accountView];
    
    //横线
    UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(0, 80, APP_VIEW_WIDTH, 1)];
    lineView.backgroundColor = APP_CELL_LINE_COLOR;
    [accountView addSubview:lineView];
    
    
    UIImageView *iv = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, 60, 60)];
    [iv sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL, [data objectForKey:@"avatarUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    //    iv.backgroundColor = [UIColor b];
    [accountView addSubview:iv];
    
    CGFloat originX = 80;
    CGFloat originY = 0;
    CGFloat sizeWidth = (APP_VIEW_WIDTH-80)/2;
    CGFloat sizeHeight = 80/4;
    //昵称
    UILabel *nickLabel = [self setAccountLabel: CGRectMake(originX, originY, APP_VIEW_WIDTH/2, sizeHeight)];
    nickLabel.text = [NSString stringWithFormat:@"%@",[data objectForKey:@"nickName"]];
    nickLabel.tag = 2000;
    [accountView addSubview:nickLabel];
    
    //手机号后四位
    UILabel *mobileNbrLabel = [self setAccountLabel: CGRectMake(originX + sizeWidth, originY, sizeWidth, sizeHeight)];
    mobileNbrLabel.text = [NSString stringWithFormat:@"手机号后四位:%@",[data objectForKey:@"mobileNbr"]];
//    mobileNbrLabel.textAlignment = NSTextAlignmentRight;
    mobileNbrLabel.tag = 2001;
    [accountView addSubview:nickLabel];
    
    originY = originY + sizeHeight;
    //消费次数
    UILabel *consumptionNbrLabel = [self setAccountLabel: CGRectMake(originX, originY, sizeWidth, sizeHeight)];
    consumptionNbrLabel.text = [NSString stringWithFormat:@"消费次数:%@",[data objectForKey:@"consumptionNbr"]];
    consumptionNbrLabel.tag = 2011;
    [accountView addSubview:consumptionNbrLabel];
    
    //消费金额
    UILabel *consumptionAmountLabel = [self setAccountLabel: CGRectMake(originX + sizeWidth, originY, sizeWidth, sizeHeight)];
    consumptionAmountLabel.text = [NSString stringWithFormat:@"消费金额:%.2f",[[data objectForKey:@"consumptionAmount"]floatValue]];
    consumptionAmountLabel.tag = 2012;
    [accountView addSubview:consumptionAmountLabel];
    
    originY = originY + sizeHeight;
    //支付金额
    UILabel *payAmountLabel = [self setAccountLabel: CGRectMake(originX, originY, sizeWidth, sizeHeight)];
    payAmountLabel.text = [NSString stringWithFormat:@"支付:%.2f",[[data objectForKey:@"payAmount"]floatValue]];
    payAmountLabel.tag = 2021;
    [accountView addSubview:payAmountLabel];
    
    //优惠金额
    UILabel *discountAmountLabel = [self setAccountLabel: CGRectMake(originX + sizeWidth, originY, sizeWidth, sizeHeight)];
    discountAmountLabel.text = [NSString stringWithFormat:@"优惠:%.2f",[[data objectForKey:@"discountAmount"]floatValue]];
    discountAmountLabel.tag = 2022;
    [accountView addSubview:discountAmountLabel];
    
    originY = originY + sizeHeight;
    //最后支付时间
    UILabel *lastConsumeTimeLabel = [self setAccountLabel: CGRectMake(originX , originY, APP_VIEW_WIDTH - 90, sizeHeight)];
    lastConsumeTimeLabel.textAlignment = NSTextAlignmentRight;
    lastConsumeTimeLabel.text = [NSString stringWithFormat:@"最后支付时间:%@",[data objectForKey:@"lastConsumeTime"]];
    lastConsumeTimeLabel.tag = 2030;
    [accountView addSubview:lastConsumeTimeLabel];

}

- (CGFloat)getCellHeight:(int)Tag {
    CGFloat cellHeight = 0;
    switch (Tag) {
            
        case 1000: //账单查询
            
            break;
        case 1100: //顾客清单
            cellHeight = 80;
            break;
        case 1101: //消费未结算账单
            
            break;
        case 1102: //支付结算对账
            
            break;
        case 1200: //退款清单
            cellHeight = 100;
            break;
        case 1201: //补贴未结算账单
            
            break;
        case 1202: //补贴结算对账
            
            break;
        default:
            break;
            
    }
    return cellHeight;
    
}




- (UILabel *)setAccountLabel:(CGRect)frame {
    
    UILabel *label = [[UILabel alloc] initWithFrame:frame];
    label.textColor = UICOLOR(101, 102, 103, 1.0);
    label.font = [UIFont systemFontOfSize:11.f];
    [accountView addSubview:label];
    
    return label;
}



@end
