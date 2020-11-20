//
//  RefundListCell.m
//  BMSQS
//
//  Created by gh on 16/3/14.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "RefundListCell.h"
#import "OpenClassUtil.h"
#import "UIColor+Tools.h"


@interface RefundListCell ()

@property (nonatomic, strong)UILabel *orderNbrLabel;//订单号
@property (nonatomic, strong)UILabel *nickNameLabel;//昵称
@property (nonatomic, strong)UILabel *mobileNbr;
@property (nonatomic, strong)UILabel *orderAmountLabel;//消费金额
@property (nonatomic, strong)UILabel *realPayLabel;//实际消费
@property (nonatomic, strong)UILabel *deductionLabel; //优惠金额
@property (nonatomic, strong)UILabel *refundAmountLabel; //退款金额
@property (nonatomic, strong)UILabel *payedTimeLabel; //消费时间
@property (nonatomic, strong)UILabel *refundTimenLabel;//退款时间
@property (nonatomic, strong)UILabel *refundReasonLabel; //退款理由
@property (nonatomic, strong)NSString *handleFlag; //商家处理结果	number	1-未处理；2-同意退款；3-不同意退款

@property (nonatomic, strong)UIView *bottomView;

@property (nonatomic, strong)UIView *lineView;

@end

@implementation RefundListCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self setViewUp];
    }
    
    return self;
}


- (void)setViewUp {
    

    
    self.userIv = [[UIImageView    alloc] initWithFrame:CGRectMake(10, 15, APP_VIEW_WIDTH/4-20, APP_VIEW_WIDTH/4-20)];
    self.userIv.backgroundColor = [UIColor whiteColor];
    [self.contentView addSubview:self.userIv];

    CGFloat originX = APP_VIEW_WIDTH/4;
    CGFloat originY = 10;
    CGFloat sizeWidth = APP_VIEW_WIDTH/4*3-10;
    
    self.nickNameLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 20) text:@"昵称" font:[UIFont systemFontOfSize:12.f] textColor:UICOLOR(101, 101, 101, 1) view:self.contentView];
    

    self.mobileNbr = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth-10, 20) text:@"手机号" font:[UIFont systemFontOfSize:12.f] textColor:UICOLOR(101, 101, 101, 1) view:self.contentView];
    self.mobileNbr.textAlignment = NSTextAlignmentRight;
    
    originY = originY + 20;
    
    self.orderAmountLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth/2, 20) text:@"消费金额" font:[UIFont systemFontOfSize:13.f] textColor:UICOLOR(51, 51, 51, 1) view:self.contentView];
    
    self.realPayLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX+sizeWidth/2, originY, sizeWidth/2, 20)  text:@"支付金额" font:[UIFont systemFontOfSize:13.f] textColor:UICOLOR(51, 51, 51, 1) view:self.contentView];
    
    originY = originY + 20;
    self.deductionLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth/2, 20)  text:@"优惠金额" font:[UIFont systemFontOfSize:13.f] textColor:UICOLOR(51, 51, 51, 1) view:self.contentView];
    
    self.refundAmountLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX+sizeWidth/2, originY, sizeWidth/2, 20)  text:@"退款金额" font:[UIFont systemFontOfSize:13.f] textColor:UICOLOR(51, 51, 51, 1) view:self.contentView];
    
    
    originY = originY + 20;
    
    self.payedTimeLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 20)  text:@"消费时间" font:[UIFont systemFontOfSize:13.f] textColor:UICOLOR(52, 52, 52, 1) view:self.contentView];
    
    
    originY = originY + 20;
    self.orderNbrLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 20)   text:@"订单号:xxxxxx" font:[UIFont systemFontOfSize:13.f] textColor:UICOLOR(52, 52, 52, 1) view:self.contentView];
    
    originY = originY + 30;
    [gloabFunction ggsetLineView:CGRectMake(0, originY, APP_VIEW_WIDTH, 1) view:self.contentView];
    
    
    originY = originY + 5;
    
    
    self.refundTimenLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 20)   text:@"退款时间:2016年2月19日 16:15" font:[UIFont systemFontOfSize:13.f] textColor:UICOLOR(52, 52, 52, 1) view:self.contentView];
    NSLog(@"%f",originY);
    originY = originY + 20;
    
    self.refundReasonLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 20)text:@"退款理由" font:[UIFont systemFontOfSize:13.f] textColor:UICOLOR(52, 52, 52, 1) view:self.contentView];
    self.refundReasonLabel.numberOfLines = 0;
    
    
    originY = originY + 20;
    self.lineView = [[UIView alloc] initWithFrame:CGRectMake(0, originY-1, APP_VIEW_WIDTH, 1)];
    self.lineView.backgroundColor = APP_CELL_LINE_COLOR;
    [self.contentView addSubview:self.lineView];
    
    

    self.bottomView = [[UIView alloc] initWithFrame:CGRectMake(0, originY, APP_VIEW_WIDTH, 70)];
    self.bottomView.backgroundColor = UICOLOR(240, 239, 245, 1);
    [self.contentView addSubview:self.bottomView];
    
    

    self.leftBtn = [UIButtonEx buttonWithType:UIButtonTypeCustom];
    self.leftBtn.frame = CGRectMake(25, 20, APP_VIEW_WIDTH/2-50, 30);
    [self.leftBtn setTitle:@"不同意退款" forState:UIControlStateNormal];
    [self.leftBtn.titleLabel setFont:[UIFont systemFontOfSize:14.f]];
    [self.leftBtn.layer setMasksToBounds:YES];
    [self.leftBtn.layer setCornerRadius:3.0];
    self.leftBtn.backgroundColor = UICOLOR(197, 1, 9, 1);
    [self.bottomView addSubview:self.leftBtn];
    [self.leftBtn addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    
    
    self.rightBtn = [UIButtonEx buttonWithType:UIButtonTypeCustom];
    self.rightBtn.tag = 1002;
    self.rightBtn.frame = CGRectMake(APP_VIEW_WIDTH/2+25, 20, APP_VIEW_WIDTH/2-50, 30);
    [self.rightBtn setTitle:@"同意退款" forState:UIControlStateNormal];
    [self.rightBtn.titleLabel setFont:[UIFont systemFontOfSize:14.f]];
    [self.rightBtn.layer setMasksToBounds:YES];
    [self.rightBtn.layer setCornerRadius:3.0];
    self.rightBtn.backgroundColor = UICOLOR(197, 1, 9, 1);
    [self.bottomView addSubview:self.rightBtn];
    [self.rightBtn addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    
    
    
    
}
//avatarUrl	用户头像	string
//deduction	优惠金额	number	单位：元
//handleFlag	商家处理结果	number	1-未处理；2-同意退款；3-不同意退款
//isToday	是不是当天的交易	number	1-是；0-不是
//mobileNbr	用户手机号	number
//nickName	昵称	string
//orderAmount	消费金额	number	单位：元
//orderNbr	订单号	string
//payedTime	消费时间	string	格式：yyyy年mm月dd日hh:ii:ss
//realPay	实际支付金额	number	单位：元
//refundAmount	退款金额	number	单位：元
//refundApplyTime	申请退款时间	string	格式：yyyy年mm月dd日hh:ii:ss
//refundCode	退款编码	string
//	退款理由	string
//rejectReason	不同意退款的理由




- (void)setCellValue:(NSDictionary *)dic {

    
    
    self.nickNameLabel.text = [NSString stringWithFormat:@"%@",[dic objectForKey:@"nickName"]];
    self.mobileNbr.text = [NSString stringWithFormat:@"%@",[dic objectForKey:@"mobileNbr"]];
    self.orderAmountLabel.text = [NSString stringWithFormat:@"消费%.2f",[[dic objectForKey:@"orderAmount"] floatValue]];
    self.realPayLabel.text = [NSString stringWithFormat:@"支付%.2f",[[dic objectForKey:@"realPay"] floatValue]];
    self.deductionLabel.text = [NSString stringWithFormat:@"优惠:%.2f",[[dic objectForKey:@"deduction"] floatValue]];
    self.refundAmountLabel.text = [NSString stringWithFormat:@"退款:%.2f", [[dic objectForKey:@"refundAmount"] floatValue]];
    self.orderNbrLabel.text = [NSString stringWithFormat:@"订单号:%@",[dic objectForKey:@"orderNbr"]];
    self.payedTimeLabel.text = [NSString stringWithFormat:@"消费时间:%@",[dic objectForKey:@"payedTime"]];
    self.refundTimenLabel.text = [NSString stringWithFormat:@"申请退款时间:%@",[dic objectForKey:@"refundApplyTime"]];
    self.refundReasonLabel.text = [NSString stringWithFormat:@"理由:%@",[dic objectForKey:@"refundReason"]];
    
    CGSize refundReasonSize = [OpenClassUtil getRefundReasonSize:self.refundReasonLabel.text];
    if (refundReasonSize.height>20) {
        self.refundReasonLabel.frame = CGRectMake(self.refundReasonLabel.frame.origin.x, 145 + (20/2-13.0/2), refundReasonSize.width, refundReasonSize.height);
    }else {
        self.refundReasonLabel.frame = CGRectMake(self.refundReasonLabel.frame.origin.x, 145, refundReasonSize.width, 20);
    }
    
    
    
    self.lineView.frame = CGRectMake(0, self.refundReasonLabel.frame.origin.y+refundReasonSize.height+20-1, APP_VIEW_WIDTH, 1);
    
    self.bottomView.frame = CGRectMake(0, self.refundReasonLabel.frame.origin.y+refundReasonSize.height+20, APP_VIEW_WIDTH, 70);
    
    self.leftBtn.object = dic;
    self.rightBtn.object = dic;
    
    
    self.handleFlag = [dic objectForKey:@"handleFlag"]; //商家处理结果	number	1-未处理；2-同意退款；3-不同意退款
    
    switch (self.handleFlag.intValue) {
        case 1:
            self.leftBtn.enabled = YES;
            self.leftBtn.backgroundColor = UICOLOR(197, 1, 9, 1);
            self.rightBtn.enabled = YES;
            self.rightBtn.backgroundColor = UICOLOR(197, 1, 9, 1);
            break;
        case 2:
            self.leftBtn.enabled = NO;
            self.leftBtn.backgroundColor = [UIColor grayColor];
            self.rightBtn.enabled = NO;
            self.rightBtn.backgroundColor = [UIColor grayColor];
            break;
        case 3:
            self.leftBtn.enabled = NO;
            self.leftBtn.backgroundColor = [UIColor grayColor];
            self.rightBtn.enabled = NO;
            self.rightBtn.backgroundColor = [UIColor grayColor];
            break;
            
        default:
            break;
    }
    
    
    
    
}



- (void)btnAction:(UIButtonEx *)button {
    
    
    if(button == self.leftBtn) {//不同意退款
        [self.refDelegate clickDisagreeBtn:button];
    }else { //同意退款
        [self.refDelegate clickAgreeBnt:button];
    }
    
}

@end
