//
//  OpenClassCell.m
//  BMSQS
//
//  Created by gh on 16/3/8.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "OpenClassCell.h"
#import "OpenClassUtil.h"
#import "UIImageView+WebCache.h"


@interface OpenClassCell () {
    CGFloat viewHeight;
}
- (void)setCellValue:(NSDictionary *)dic forRow:(int)row;

@property (nonatomic, strong)UIView *backView;
@property (nonatomic, strong)UILabel *leftLabel;
@property (nonatomic, strong)UILabel *rightLabel;


//@property (nonatomic, strong)UILabel *classNameLabel; //所开班级
//@property (nonatomic, strong)UILabel *learnDateLabel; //开始结束时间
//@property (nonatomic, strong)UILabel *classWeekInfoLabel;//上课时间
//@property (nonatomic, strong)UILabel *learnMemoLabel; //适合年龄段
//@property (nonatomic, strong)UILabel *learnFeeLabel;//报名费用
//@property (nonatomic, strong)UILabel *learnNumLabel;//所学课时
//@property (nonatomic, strong)UILabel *teacherNameLabel;//老师名字
//@property (nonatomic, strong)UILabel *signDateLabel;//报名开始结束时间
//@property (nonatomic, strong)UILabel *infolabel;//课程简介
//@property (nonatomic, strong)UIImageView *classImage; //展示图片
//
//@property (nonatomic, strong)UIView *sublineView;
//
//@property (nonatomic, strong)NSString *classCode;

@property (nonatomic, strong)UIView *lineView;

@end

@implementation OpenClassCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if(self) {
        [self setViewUp];
    }
    
    return self;
}

- (void)setViewUp {
    viewHeight = 40.0;
    self.backView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, viewHeight)];
    //    self.backView.backgroundColor = [UIColor blueColor];
//    self.backView.layer.borderWidth = 1.0;
//    self.backView.layer.borderColor = APP_CELL_LINE_COLOR.CGColor;
    [self.contentView addSubview:self.backView];
    
    
    self.leftLabel = [OpenClassUtil openClassSetLabel:CGRectMake(10, 0, 100, viewHeight) text:@"XX" font:[UIFont systemFontOfSize:13.f] textColor:[UIColor blackColor] view:self.backView];
    self.rightLabel = [OpenClassUtil openClassSetLabel:CGRectMake(110, 0, APP_VIEW_WIDTH-120, viewHeight) text:@"XX" font:[UIFont systemFontOfSize:13.f] textColor:[UIColor blackColor] view:self.backView];
    
    self.buttonEx = [UIButtonEx buttonWithType:UIButtonTypeCustom];
    self.buttonEx.frame = CGRectMake(APP_VIEW_WIDTH-60, 5, 50, 30);
    [self.buttonEx addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
    [self.buttonEx setBackgroundColor:[UIColor clearColor]];
    [self.buttonEx setTitle:@"删除" forState:UIControlStateNormal];
    [self.buttonEx.titleLabel setFont:[UIFont systemFontOfSize:13.f]];
    [self.buttonEx setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
    [self.contentView addSubview:self.buttonEx];
    self.buttonEx.hidden = YES;
    
    
    self.lineView = [[UIView alloc] initWithFrame:CGRectMake(0, viewHeight-0.5, APP_VIEW_WIDTH, 0.5)];
    self.lineView.backgroundColor = APP_CELL_LINE_COLOR;
    [self.contentView addSubview:self.lineView];
    
    
    
    
}



- (void)setCellValue:(NSDictionary *)dic forRow:(int)row{
    NSArray *leftAry = [NSArray arrayWithObjects:@"所开班级", @"学习期间", @"", @"适合年龄段", @"报名费用", @"所学课时", @"任课老师", @"报名时间", @"", @"", @"", nil];
    
    self.leftLabel.text = leftAry[row];

    NSString *rightStr;
    switch (row) {
        case 0:
            
            rightStr = [NSString stringWithFormat:@"%@",[dic objectForKey:@"className"]];
            break;
        case 1:
            rightStr = [NSString stringWithFormat:@"%@至%@", [dic objectForKey:@"learnStartDate"], [dic objectForKey:@"learnEndDate"]];
            break;
        case 3:
            rightStr = [NSString stringWithFormat:@"%@", [dic objectForKey:@"learnMemo"]];
            break;
        case 4:
            rightStr = [NSString stringWithFormat:@"%@", [dic objectForKey:@"learnFee"]];
            break;
        case 5:
            rightStr = [NSString stringWithFormat:@"%@", [dic objectForKey:@"learnNum"]];
            break;
        case 6:
            rightStr = [NSString stringWithFormat:@"%@", [dic objectForKey:@"teacherName"]];
            break;
        case 7:
            rightStr = [NSString stringWithFormat:@"%@至%@", [dic objectForKey:@"signStartDate"], [dic objectForKey:@"signEndDate"]];
            break;
        case 8:
            
            break;
        case 9:
            
            break;
        case 10:
            
            break;
            
        default:
            break;
    }
    if (row == 0) {
        self.buttonEx.hidden = NO;
        self.buttonEx.object = [NSString stringWithFormat:@"%@",[dic objectForKey:@"classCode"]];
    }else {
        self.buttonEx.hidden = YES;
    }
    
    
    self.rightLabel.text = rightStr;
    
}


- (void)btnAct:(UIButtonEx *)btn {
    
    if (self.openCellDelegate != nil) {
        [self.openCellDelegate btnDelShopClass:btn.object];
        
    }
    
    
}


/*
- (void)setViewUp {
    
    self.backView = [[UIView alloc] initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH-20, 100)];
//    self.backView.backgroundColor = [UIColor blueColor];
    self.backView.layer.borderWidth = 1.0;
    self.backView.layer.borderColor = APP_CELL_LINE_COLOR.CGColor;
    [self.contentView addSubview:self.backView];
//    self.contentView.backgroundColor = [ UIColor redColor];
    
    NSArray *array = [NSArray arrayWithObjects:@"所开班级", @"学习期间", @"上课时间", @"适合年龄段", @"报名费用", @"所学课时", @"任课老师", @"报名时间", nil];
    
    CGFloat originY = 0;
    CGFloat originX = 10;
    CGFloat sizeWidth = 100;

    for (int i=0; i<8; i++) {
        [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30)  text:array[i] view:self.backView];
        
        originY = originY+30;
        //画线
        [gloabFunction ggsetLineView:CGRectMake(0, originY-1, APP_VIEW_WIDTH-20, 1) view:self.backView];
        
    }
    [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30)  text:@"课程简介" view:self.backView];
    
    self.sublineView = [gloabFunction ggsetLineView:CGRectMake(0, originY-1, APP_VIEW_WIDTH-20, 1) view:self.backView];
    
    originX = self.backView.frame.size.width/3;
    originY = 0;
    sizeWidth = self.backView.frame.size.width/3*2;
    
    //所开班级
    self.classNameLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    
    originY = originY + 30;
    //学习期间
    self.learnDateLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    
    originY = originY + 30;
    //上课时间
    self.classWeekInfoLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    
    originY = originY + 30;
    //适合年龄段
    self.learnMemoLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];


    originY = originY + 30;
    //报名费用
    self.learnFeeLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    
    originY = originY + 30;
    //所学课时
    self.learnNumLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    
    originY = originY + 30;
    
    //任课老师
    self.teacherNameLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    
    originY = originY + 30;
    //报名时间
    self.signDateLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    
    originY = originY + 30;
    
    //课程简介
    self.infolabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    self.infolabel.numberOfLines = 0;
    
    originY = originY + 30;
    
    self.classImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, self.backView.frame.size.width, 80)];
    self.classImage.backgroundColor = [UIColor blueColor];
    [self.backView addSubview:self.classImage];
    
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(APP_VIEW_WIDTH-20 - 70, 2, 60, 26);
    [button setTitle:@"删除" forState:UIControlStateNormal];
    [button.titleLabel setFont:[UIFont systemFontOfSize:13.f]];
    [button setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [button addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [self.backView addSubview:button];
    
}

- (void)setCellValue:(NSDictionary *)dic {
    
    self.classCode = [NSString stringWithFormat:@"%@",[dic objectForKey:@"classCode"]];
    
    self.classNameLabel.text = [NSString stringWithFormat:@"%@",[dic objectForKey:@"className"]];
    self.learnDateLabel.text = [NSString stringWithFormat:@"%@至%@", [dic objectForKey:@"learnStartDate"], [dic objectForKey:@"learnEndDate"]];
    
    self.classWeekInfoLabel.text = [NSString stringWithFormat:@"%@",[dic objectForKey:@"className"]];
    
    
    
    
    self.learnMemoLabel.text = [NSString stringWithFormat:@"%@", [dic objectForKey:@"learnMemo"]];
    self.learnFeeLabel.text = [NSString stringWithFormat:@"%@", [dic objectForKey:@"learnFee"]];
    self.learnNumLabel.text = [NSString stringWithFormat:@"%@", [dic objectForKey:@"learnNum"]];
    self.teacherNameLabel.text = [NSString stringWithFormat:@"%@", [dic objectForKey:@"teacherName"]];
    self.signDateLabel.text = [NSString stringWithFormat:@"%@至%@", [dic objectForKey:@"signStartDate"], [dic objectForKey:@"signEndDate"]];
    self.infolabel.text= [NSString stringWithFormat:@"%@", [dic objectForKey:@"classInfo"]];
    
//    self.infolabel.text = @"撒旦连卡佛我违法提拔上来后突然发生地方国家非人工水电费公司的符合三天后Seth颜色统一浑身疼也好色呀晒太阳晒太阳色还有色体育身体容易身体容易对人体有的人他非要的热土有的人图导入的人视图";
    [self.classImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL, [dic objectForKey:@"classUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    
    
    CGSize size = [OpenClassUtil getInfolabelSize:self.infolabel.text];
    self.infolabel.frame = CGRectMake(self.infolabel.frame.origin.x, 30*8 + (30/2-13.0/2), size.width, size.height);
    
    

    self.sublineView.frame = CGRectMake(0, self.infolabel.frame.size.height + self.infolabel.frame.origin.y-1, APP_VIEW_WIDTH-20, 1);
//    self.sublineView.backgroundColor = [UIColor redColor];
    
    self.classImage.frame = CGRectMake(0, self.sublineView.frame.origin.y+5, APP_VIEW_WIDTH-20, 80);
    
    self.backView.frame = CGRectMake(10, 10, APP_VIEW_WIDTH-20, 30*8 + self.infolabel.frame.size.height + (30/2-13.0/2) + 80 + 5);
    
}




- (void)btnAction:(UIButton *)button {
    __weak typeof(self) weakSelf = self;
    
    if (weakSelf.openCellDelegate != nil) {
        
        [weakSelf.openCellDelegate btnDelShopClass:weakSelf.classCode];
    }
    
}

*/

@end
