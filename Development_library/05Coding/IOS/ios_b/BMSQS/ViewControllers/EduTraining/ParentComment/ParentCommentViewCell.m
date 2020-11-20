//
//  ParentCommentViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/10.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "ParentCommentViewCell.h"
#import "UIImageView+WebCache.h"

@interface ParentCommentViewCell ()<UITextViewDelegate>

@property (nonatomic, strong)UIView * iconView;
@property (nonatomic, strong)UIImageView * iconImage;//用户头像
@property (nonatomic, strong)UILabel     * userName;// 用户姓名
@property (nonatomic, strong)UILabel     * commentTime;//评论时间
@property (nonatomic, strong)UILabel     * grade;//打分
@property (nonatomic, strong)UIImageView     * starLevel1;// 一星
@property (nonatomic, strong)UIImageView     * starLevel2;// 二星
@property (nonatomic, strong)UIImageView     * starLevel3;// 三星
@property (nonatomic, strong)UIImageView     * starLevel4;// 四星
@property (nonatomic, strong)UIImageView     * starLevel5;// 五星
@property (nonatomic, strong)UILabel     * commentContent;//评论内容
@property (nonatomic, strong)RemarkImgView * remarkImgView;// 评论图片
@property (nonatomic, strong)UIView * replyView;  // 回复视图
@property (nonatomic, strong)UITextView * replyConent;//回复内容
@property (nonatomic, strong)UIButtonEx * replyBut;// 回复按钮
@property (nonatomic, strong)UILabel * replyTitle; //

@property (nonatomic, strong)UILabel    * shopCommentTime;// 商家回复时间
@property (nonatomic, strong)UILabel * shopReplyLB; // 商家回复
@property (nonatomic, strong)UILabel * shopReplyTitle;// 商家回复内容
@property (nonatomic, assign)BOOL isReply;

@end


@implementation ParentCommentViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}



- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        [self setCellParentComment];
    }
    return self;
}

- (void)setCellParentComment
{
    self.iconView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 80, 270)];
//    self.iconView.layer.borderColor = [UIColor grayColor].CGColor;
//    self.iconView.layer.borderWidth =1.0;
    self.iconView.backgroundColor = [UIColor clearColor];
    [self addSubview:self.iconView];
    
    self.iconImage = [[UIImageView alloc] initWithFrame:CGRectMake(5, 10, 70, 70)];
    self.iconImage.layer.cornerRadius = 10;
    self.iconImage.layer.masksToBounds = YES;
    self.iconImage.image = [UIImage imageNamed:@"share"];
    [self.iconView addSubview:self.iconImage];
    
    self.userName = [[UILabel alloc] initWithFrame:CGRectMake(90, 10, APP_VIEW_WIDTH - 200, 15)];
    self.userName.font = [UIFont systemFontOfSize:14.0];
    self.userName.text = @"用户昵称";
    [self addSubview:self.userName];
    
    self.commentTime = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH - 90, 10, 90, 15)];
    self.commentTime.font = [UIFont systemFontOfSize:12.0];
    self.commentTime.text = @"2月18日";
    [self addSubview:self.commentTime];
    
    
    self.starLevel1 = [[UIImageView alloc] initWithFrame:CGRectMake(90, 30, 15, 15)];
    self.starLevel1.image = [UIImage imageNamed:@"star_no_pick"];
    [self addSubview:self.starLevel1];
    
    self.starLevel2 = [[UIImageView alloc] initWithFrame:CGRectMake(107, 30, 15, 15)];
    self.starLevel2.image = [UIImage imageNamed:@"star_no_pick"];
    [self addSubview:self.starLevel2];
    
    self.starLevel3 = [[UIImageView alloc] initWithFrame:CGRectMake(124, 30, 15, 15)];
    self.starLevel3.image = [UIImage imageNamed:@"star_no_pick"];
    [self addSubview:self.starLevel3];
    
    self.starLevel4 = [[UIImageView alloc] initWithFrame:CGRectMake(141, 30, 15, 15)];
    self.starLevel4.image = [UIImage imageNamed:@"star_no_pick"];
    [self addSubview:self.starLevel4];
    
    self.starLevel5 = [[UIImageView alloc] initWithFrame:CGRectMake(158, 30, 15, 15)];
    self.starLevel5.image = [UIImage imageNamed:@"star_no_pick"];
    [self addSubview:self.starLevel5];
    
    self.commentContent = [[UILabel alloc] initWithFrame:CGRectMake(90, 55, APP_VIEW_WIDTH -  100, 20)];
    self.commentContent.font = [UIFont systemFontOfSize:12.0];
    self.commentContent.numberOfLines = 0;
    [self addSubview:self.commentContent];
    
    [self setRemarkView];
    
    [self setCellReplyView];
    
    
}

- (void)setRemarkView {
    self.remarkImgView = [[RemarkImgView alloc] initWithFrame:CGRectMake(80, 70 + self.commentContent.frame.size.height, APP_VIEW_WIDTH - 80, 90)];
    self.remarkImgView.backgroundColor = [UIColor clearColor]; // 横向滑动的视图
    [self addSubview:self.remarkImgView];
}

- (void)setCellReplyView
{
    self.replyView = [[UIView alloc] initWithFrame:CGRectMake(80, self.iconView.frame.size.height - 60, APP_VIEW_WIDTH - 80, 60)];
//    self.replyView.layer.borderColor = [UIColor grayColor].CGColor;
//    self.replyView.layer.borderWidth =1.0;
    self.replyView.backgroundColor = APP_VIEW_BACKCOLOR;
    [self addSubview:self.replyView];
    
    self.replyConent = [[UITextView alloc] initWithFrame:CGRectMake(10, 5, self.replyView.frame.size.width - 75, self.replyView.frame.size.height - 10)];
    self.replyConent.backgroundColor = APP_VIEW_BACKCOLOR;
    self.replyConent.textColor = UICOLOR(71, 71, 71, 1.0);
    self.replyConent.delegate = self;
    [self.replyView addSubview:self.replyConent];
    
    self.replyBut = [UIButtonEx buttonWithType:UIButtonTypeCustom];
    self.replyBut.frame = CGRectMake(self.replyView.frame.size.width - 50, 8, 25, 25);
    //    [self.replyBut setTitle:@"回复" forState:UIControlStateNormal];
    [self.replyBut setImage:[UIImage imageNamed:@"huifu"] forState:UIControlStateNormal];
    [self.replyBut addTarget:self action:@selector(replyButClick:) forControlEvents:UIControlEventTouchUpInside];
    [self.replyView addSubview:self.replyBut];
    
    self.replyTitle = [[UILabel alloc] initWithFrame:CGRectMake(self.replyView.frame.size.width - 52, 38, 28, 15)];
    self.replyTitle.textAlignment = NSTextAlignmentCenter;
    self.replyTitle.text = @"回复";
    self.replyTitle.font = [UIFont systemFontOfSize:13.0];
    self.replyTitle.textColor = UICOLOR(71, 71, 71, 1.0);
    [self.replyView addSubview:self.replyTitle];
    
    self.shopCommentTime = [[UILabel alloc] initWithFrame:CGRectMake(self.replyView.frame.size.width - 70, 3, 70, 15)];
    self.shopCommentTime.textColor = UICOLOR(71, 71, 71, 1.0);
    self.shopCommentTime.font = [UIFont systemFontOfSize:12.0];
    [self.replyView addSubview:self.shopCommentTime];
    // shopReplyLB shopReplyTitle
    
    self.shopReplyLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 3, 80, 15)];
    self.shopReplyLB.text = @"商家回复:";
    self.shopReplyLB.font = [UIFont systemFontOfSize:12.0];
    self.shopReplyLB.textColor = UICOLOR(71, 71, 71, 1.0);
    [self.replyView addSubview:self.shopReplyLB];


}

- (void)setCellParentCommentDic:(NSDictionary *)commentDic
{
    
    [self.iconImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,commentDic[@"avatarUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    
    self.remarkCode = [NSString stringWithFormat:@"%@", commentDic[@"remarkCode"]];
    self.userName.text = [NSString stringWithFormat:@"%@", commentDic[@"nickName"]];
    NSString * YearStr = [commentDic[@"remarkTime"] substringWithRange:NSMakeRange(0, 4)];
    NSString * MontStr = [commentDic[@"remarkTime"] substringWithRange:NSMakeRange(5, 2)];
    NSString * DayStr = [commentDic[@"remarkTime"] substringWithRange:NSMakeRange(8, 2)];
    self.commentTime.text = [NSString stringWithFormat:@"%@-%@-%@",YearStr,MontStr ,DayStr];
    
    NSString *  wholeLvl = [NSString stringWithFormat:@"%@", commentDic[@"wholeLvl"]];
    if (wholeLvl.intValue == 1) {
        self.starLevel1.image = [UIImage imageNamed:@"start_pick"];
    }else if (wholeLvl.intValue == 2){
        self.starLevel1.image = [UIImage imageNamed:@"start_pick"];
        self.starLevel2.image = [UIImage imageNamed:@"start_pick"];
    }else if (wholeLvl.intValue == 3){
        self.starLevel1.image = [UIImage imageNamed:@"start_pick"];
        self.starLevel2.image = [UIImage imageNamed:@"start_pick"];
        self.starLevel3.image = [UIImage imageNamed:@"start_pick"];
    }else if (wholeLvl.intValue == 4){
        self.starLevel1.image = [UIImage imageNamed:@"start_pick"];
        self.starLevel2.image = [UIImage imageNamed:@"start_pick"];
        self.starLevel3.image = [UIImage imageNamed:@"start_pick"];
        self.starLevel4.image = [UIImage imageNamed:@"start_pick"];
    }else if (wholeLvl.intValue == 5){
        self.starLevel1.image = [UIImage imageNamed:@"start_pick"];
        self.starLevel2.image = [UIImage imageNamed:@"start_pick"];
        self.starLevel3.image = [UIImage imageNamed:@"start_pick"];
        self.starLevel4.image = [UIImage imageNamed:@"start_pick"];
        self.starLevel5.image = [UIImage imageNamed:@"start_pick"];
    }
    
    self.commentContent.text = [NSString stringWithFormat:@"    %@", commentDic[@"remark"]];
    CGSize size = [self.commentContent.text boundingRectWithSize:CGSizeMake(self.commentContent.frame.size.width, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: self.commentContent.font} context:nil].size;
    self.commentContent.frame = CGRectMake(90, 55, APP_VIEW_WIDTH -  100, size.height);
    if (self.remarkImgView != nil) {
        [self.remarkImgView removeFromSuperview];
        [self setRemarkView];
    }
    
    if (self.replyView != nil) {
        [self.replyView removeFromSuperview];
        [self setCellReplyView];
    }
    
    self.remarkImgView.frame = CGRectMake(80, 70 + self.commentContent.frame.size.height, APP_VIEW_WIDTH - 80, 90);
    self.iconView.frame = CGRectMake(0, 0, 80, 230 + size.height);
    self.replyView.frame = CGRectMake(80, self.iconView.frame.size.height - 60, APP_VIEW_WIDTH - 80, 60);
    
    
    [self.remarkImgView setRemarkWithAry:commentDic[@"classRemarkImg"]];
    if ([commentDic[@"classRemarkImg"] count] == 0) {
        self.remarkImgView.hidden = YES;
        self.iconView.frame = CGRectMake(0, 0, 80, 130 + size.height);
        self.replyView.frame = CGRectMake(80, self.iconView.frame.size.height - 60, APP_VIEW_WIDTH - 80, 60);
    }

    
    NSString * isRemarkByShop = [NSString stringWithFormat:@"%@", commentDic[@"isRemarkByShop"]];
    if (isRemarkByShop.intValue == 0) {
        self.isReply = NO;
        self.replyBut.hidden = NO;
        self.replyTitle.hidden = NO;
        self.replyConent.hidden = NO;
        self.replyBut.page = 0;
        self.shopCommentTime.hidden = YES;
        self.shopReplyLB.hidden = YES;
        self.replyConent.backgroundColor = [UIColor whiteColor];
        self.replyConent.frame = CGRectMake(10, 5, self.replyView.frame.size.width - 75, self.replyView.frame.size.height - 10);
    }else if (isRemarkByShop.intValue == 1){
        self.replyConent.text = [NSString stringWithFormat:@"   %@", commentDic[@"shopRemark"]];
        self.isReply = YES;
        self.replyConent.backgroundColor = APP_VIEW_BACKCOLOR;
        self.replyConent.frame = CGRectMake(0, 20, self.replyView.frame.size.width, self.replyView.frame.size.height - 20);
        self.replyBut.hidden = YES;
        self.replyTitle.hidden = YES;
        self.replyConent.hidden = NO;
        self.replyBut.page = 1;
        self.shopCommentTime.hidden = NO;
        NSString * shopYearStr = [commentDic[@"shopRemarkTime"] substringWithRange:NSMakeRange(0, 4)];
        NSString * shopMontStr = [commentDic[@"shopRemarkTime"] substringWithRange:NSMakeRange(5, 2)];
        NSString * shopDayStr = [commentDic[@"shopRemarkTime"] substringWithRange:NSMakeRange(8, 2)];
        self.shopCommentTime.text = [NSString stringWithFormat:@"%@-%@-%@",shopYearStr,shopMontStr ,shopDayStr];
        self.shopReplyLB.hidden = NO;
        
    }
    
    
    
}

- (void)replyButClick:(UIButtonEx *)sender
{
    if (sender.page == 0) {
        if ([self.parentDelegate respondsToSelector:@selector(setParentWithRemarkCode:shopRemark:)]) {
            
            [self.parentDelegate setParentWithRemarkCode:self.remarkCode shopRemark:self.replyConent.text];
        }
        
        
    }
}

- (BOOL)textViewShouldBeginEditing:(UITextView *)textView
{
    if (self.isReply) {
        return NO;
    }else{
        return YES;
    }
}


@end
